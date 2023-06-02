package com.shpp.ahrokholska.basicapplication.data.repository.userNetwork

import com.google.gson.Gson
import com.shpp.ahrokholska.basicapplication.data.utils.Constants.AUTHORIZATION_HEADER
import com.shpp.ahrokholska.basicapplication.data.utils.Constants.RESPONSE_ERRORS
import com.shpp.ahrokholska.basicapplication.domain.model.InputErrorNetworkResponse
import com.shpp.ahrokholska.basicapplication.domain.model.NetworkErrorNetworkResponse
import com.shpp.ahrokholska.basicapplication.domain.model.NetworkResponse
import com.shpp.ahrokholska.basicapplication.domain.model.SuccessNetworkResponse
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
                return@withContext processError(e)
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
            SuccessNetworkResponse(user)
        }

    override suspend fun getUser(id: Long, refreshToken: String): NetworkResponse<User> =
        withContext(Dispatchers.IO) {
            when (val tokenResponse = refreshToken(refreshToken)) {
                is NetworkErrorNetworkResponse -> NetworkErrorNetworkResponse()
                is InputErrorNetworkResponse -> InputErrorNetworkResponse()
                is SuccessNetworkResponse -> {
                    with(tokenResponse.data) {
                        val response = getUserByAccessToken(id, accessToken, this.refreshToken)
                        if (response is SuccessNetworkResponse<User>) {
                            cachedUser = response.data
                        }
                        response
                    }
                }
            }
        }

    override suspend fun createUser(
        email: String, password: String, name: String?, phone: String?
    ) = withContext(Dispatchers.IO) {
        val body: String
        try {
            body = service.createUser(email, password, name, phone).string()
        } catch (e: Exception) {
            return@withContext processError<User>(e)
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
        SuccessNetworkResponse(user)
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
            return@withContext processError<User>(e)
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
        SuccessNetworkResponse(user)
    }

    private suspend fun getUserByAccessToken(
        id: Long, accessToken: String, refreshToken: String
    ): NetworkResponse<User> =
        withContext(Dispatchers.IO) {
            val body: String
            try {
                body = service.getUser(id, AUTHORIZATION_HEADER + accessToken).string()
            } catch (e: Exception) {
                return@withContext processError<User>(e)
            }

            val con = parseBody<ResponseUser>(body)
            with(con.data.user) {
                SuccessNetworkResponse(
                    User(
                        this.id, name, career, phone, address, birthday, accessToken, refreshToken
                    )
                )
            }
        }

    private suspend fun refreshToken(refreshToken: String): NetworkResponse<DataResponseTokens> =
        withContext(Dispatchers.IO) {
            val body: String
            try {
                body = service.refreshToken(refreshToken).string()
            } catch (e: Exception) {
                return@withContext processError<DataResponseTokens>(e)
            }

            val con = parseBody<ResponseTokens>(body)
            SuccessNetworkResponse(DataResponseTokens(con.data.accessToken, con.data.refreshToken))
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

    private fun <T> processError(exception: Exception): NetworkResponse<T> {
        return when (exception) {
            is CancellationException -> throw exception

            is retrofit2.HttpException -> {
                if (RESPONSE_ERRORS.values().any { it.code == exception.code() }) {
                    InputErrorNetworkResponse()
                } else {
                    NetworkErrorNetworkResponse()
                }
            }

            else -> {
                NetworkErrorNetworkResponse()
            }
        }
    }

}