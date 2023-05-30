package com.shpp.ahrokholska.basicapplication.data.repository.userNetwork

import com.google.gson.Gson
import com.shpp.ahrokholska.basicapplication.data.utils.Constants.AUTHORIZATION_HEADER
import com.shpp.ahrokholska.basicapplication.data.utils.Constants.RESPONSE_ERRORS
import com.shpp.ahrokholska.basicapplication.domain.model.NetworkResponse
import com.shpp.ahrokholska.basicapplication.domain.model.NetworkResponseCode
import com.shpp.ahrokholska.basicapplication.domain.model.User
import com.shpp.ahrokholska.basicapplication.domain.repository.userRepository.UserNetworkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserNetworkRepositoryImpl @Inject constructor(private val service: UserNetworkService) :
    UserNetworkRepository {

    override suspend fun authorizeUser(email: String, password: String): NetworkResponse<User> =
        withContext(Dispatchers.IO) {
            val body: String
            try {
                body = service.authorizeUser(UserCred(email, password)).string()
            } catch (e: Exception) {
                return@withContext processError(e)
            }

            val con = parseBody<ResponseUserPlusToken>(body)
            NetworkResponse(
                NetworkResponseCode.Success,
                User(
                    con.data.user.id,
                    con.data.user.name,
                    con.data.user.career,
                    con.data.user.phone,
                    con.data.user.address,
                    con.data.user.birthday,
                    con.data.accessToken,
                    con.data.refreshToken
                )
            )
        }

    override suspend fun getUser(id: Long, refreshToken: String): NetworkResponse<User> =
        withContext(Dispatchers.IO) {
            val tokenResponse = refreshToken(refreshToken)
            if (tokenResponse.code != NetworkResponseCode.Success) {
                return@withContext NetworkResponse<User>(tokenResponse.code, null)
            }
            with(tokenResponse.data) {
                getUserByAccessToken(id, this?.accessToken.orEmpty(), this?.refreshToken.orEmpty())
            }
        }

    private suspend fun getUserByAccessToken(
        id: Long, accessToken: String, refreshToken: String
    ): NetworkResponse<User> =
        withContext(Dispatchers.IO) {
            val body: String
            try {
                body = service.getUser(id, AUTHORIZATION_HEADER + accessToken).string()
            } catch (e: Exception) {
                return@withContext processError(e)
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
                return@withContext processError(e)
            }

            val con = parseBody<ResponseTokens>(body)
            NetworkResponse(
                NetworkResponseCode.Success,
                DataResponseTokens(con.data.accessToken, con.data.refreshToken)
            )
        }

    private inline fun <reified T> parseBody(body: String): T {
        var newBody = body
        if (body[body.length - 2] != '}') { //some responses don't have closing '}' at the end =/
            newBody += '}'
        }
        return Gson().fromJson(newBody, T::class.java)
    }

    private fun <T> processError(exception: Exception): NetworkResponse<T> {
        return if (exception is retrofit2.HttpException) {
            if (RESPONSE_ERRORS.values().any { it.code == exception.code() }) {
                NetworkResponse(NetworkResponseCode.InputError, null)
            } else {
                NetworkResponse(NetworkResponseCode.NetworkError, null)
            }
        } else {
            NetworkResponse(NetworkResponseCode.NetworkError, null)
        }
    }

}