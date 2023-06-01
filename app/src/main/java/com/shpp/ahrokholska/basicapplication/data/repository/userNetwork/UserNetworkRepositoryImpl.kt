package com.shpp.ahrokholska.basicapplication.data.repository.userNetwork

import com.google.gson.Gson
import com.shpp.ahrokholska.basicapplication.data.utils.Constants.AUTHORIZATION_HEADER
import com.shpp.ahrokholska.basicapplication.data.utils.Constants.RESPONSE_ERRORS
import com.shpp.ahrokholska.basicapplication.domain.model.NetworkResponse
import com.shpp.ahrokholska.basicapplication.domain.model.NetworkResponseCode
import com.shpp.ahrokholska.basicapplication.domain.model.User
import com.shpp.ahrokholska.basicapplication.domain.repository.userRepository.UserNetworkRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserNetworkRepositoryImpl @Inject constructor(private val service: UserNetworkService) :
    UserNetworkRepository {
    private var cachedUser: User? = null

    override fun getCachedUser(): User? = cachedUser

    override suspend fun authorizeUser(email: String, password: String): NetworkResponse<User> =
        withContext(Dispatchers.IO) {
            val body: String
            try {
                body = service.authorizeUser(UserCredentials(email, password)).string()
            } catch (e: Exception) {
                val code = processError(e)
                return@withContext NetworkResponse<User>(code, User())
            }

            val con = parseBody<ResponseUserPlusToken>(body)
            val user = User(
                con.data.user.id,
                con.data.user.name,
                con.data.user.career,
                con.data.user.phone,
                con.data.user.address,
                con.data.user.birthday,
                con.data.accessToken,
                con.data.refreshToken
            )
            cachedUser = user
            NetworkResponse(NetworkResponseCode.Success, user)
        }

    override suspend fun getUser(id: Long, refreshToken: String): NetworkResponse<User> =
        withContext(Dispatchers.IO) {
            val tokenResponse = refreshToken(refreshToken)
            if (tokenResponse.code != NetworkResponseCode.Success) {
                return@withContext NetworkResponse<User>(tokenResponse.code, User())
            }
            with(tokenResponse.data) {
                val response = getUserByAccessToken(id, accessToken, this.refreshToken)
                if (response.code == NetworkResponseCode.Success) {
                    cachedUser = response.data
                }
                response
            }
        }

    override suspend fun createUser(
        email: String, password: String, name: String?, phone: String?
    ) = withContext(Dispatchers.IO) {
        val body: String
        try {
            body = service.createUser(email, password, name, phone).string()
        } catch (e: Exception) {
            val code = processError(e)
            return@withContext NetworkResponse<User>(code, User())
        }

        val con = parseBody<ResponseUserPlusToken>(body)
        val user = User(
            con.data.user.id,
            con.data.user.name,
            con.data.user.career,
            con.data.user.phone,
            con.data.user.address,
            con.data.user.birthday,
            con.data.accessToken,
            con.data.refreshToken
        )
        cachedUser = user
        NetworkResponse(NetworkResponseCode.Success, user)
    }

    override suspend fun editUser(
        id: Long, accessToken: String, refreshToken: String, name: String, career: String?,
        phone: String, address: String?, date: String?
    ): NetworkResponse<User> = withContext(Dispatchers.IO) {
        val body: String
        try {
            body = service.editUser(
                id, AUTHORIZATION_HEADER + accessToken,
                name, career, phone, address, date
            ).string()
        } catch (e: Exception) {
            val code = processError(e)
            return@withContext NetworkResponse<User>(code, User())
        }

        val con = parseBody<ResponseUser>(body)
        val user = User(
            con.data.user.id,
            con.data.user.name,
            con.data.user.career,
            con.data.user.phone,
            con.data.user.address,
            con.data.user.birthday,
            accessToken,
            refreshToken
        )
        cachedUser = user
        NetworkResponse(NetworkResponseCode.Success, user)
    }

    private suspend fun getUserByAccessToken(
        id: Long, accessToken: String, refreshToken: String
    ): NetworkResponse<User> =
        withContext(Dispatchers.IO) {
            val body: String
            try {
                body = service.getUser(id, AUTHORIZATION_HEADER + accessToken).string()
            } catch (e: Exception) {
                val code = processError(e)
                return@withContext NetworkResponse<User>(code, User())
            }

            val con = parseBody<ResponseUser>(body)
            with(con.data.user) {
                NetworkResponse(
                    NetworkResponseCode.Success,
                    User(
                        this.id, name, career, phone, address, birthday, accessToken, refreshToken
                    )
                )
            }
        }

    private suspend fun refreshToken(refreshToken: String) =
        withContext(Dispatchers.IO) {
            val body: String
            try {
                body = service.refreshToken(refreshToken).string()
            } catch (e: Exception) {
                val code = processError(e)
                return@withContext NetworkResponse<DataResponseTokens>(
                    code, DataResponseTokens("", "")
                )
            }

            val con = parseBody<ResponseTokens>(body)
            NetworkResponse(
                NetworkResponseCode.Success,
                DataResponseTokens(con.data.accessToken, con.data.refreshToken)
            )
        }

    private inline fun <reified T> parseBody(body: String): T {
        var newBody = body
        val openBrackets = body.count { it == '{' }
        val closeBrackets = body.count { it == '}' }
        if (openBrackets != closeBrackets) { //some responses don't have closing '}' at the end =/
            newBody += '}'
        }
        return Gson().fromJson(newBody, T::class.java)
    }

    private fun processError(exception: Exception): NetworkResponseCode {
        return when (exception) {
            is CancellationException -> throw exception

            is retrofit2.HttpException -> {
                if (RESPONSE_ERRORS.values().any { it.code == exception.code() }) {
                    NetworkResponseCode.InputError
                } else {
                    NetworkResponseCode.NetworkError
                }
            }

            else -> {
                NetworkResponseCode.NetworkError
            }
        }
    }

}