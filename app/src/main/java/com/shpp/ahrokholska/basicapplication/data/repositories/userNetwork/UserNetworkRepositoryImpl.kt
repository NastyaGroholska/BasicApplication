package com.shpp.ahrokholska.basicapplication.data.repositories.userNetwork

import com.shpp.ahrokholska.basicapplication.data.model.ResponseTokens
import com.shpp.ahrokholska.basicapplication.data.model.ResponseUser
import com.shpp.ahrokholska.basicapplication.data.model.ResponseUserPlusToken
import com.shpp.ahrokholska.basicapplication.data.model.UserCredentials
import com.shpp.ahrokholska.basicapplication.data.utils.Constants.AUTHORIZATION_HEADER
import com.shpp.ahrokholska.basicapplication.data.utils.ErrorHandler.processError
import com.shpp.ahrokholska.basicapplication.data.utils.Parser.parseBody
import com.shpp.ahrokholska.basicapplication.domain.model.NetworkResponse
import com.shpp.ahrokholska.basicapplication.domain.model.User
import com.shpp.ahrokholska.basicapplication.domain.repository.userRepository.UserNetworkRepository
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
            val user = con.data.user.toUser(con.data.accessToken, con.data.refreshToken)
            cachedUser = user
            NetworkResponse.Success(user)
        }

    override suspend fun getUser(id: Long, refreshToken: String): NetworkResponse<User> =
        withContext(Dispatchers.IO) {
            when (val tokenResponse = refreshToken(refreshToken)) {
                is NetworkResponse.NetworkError -> NetworkResponse.NetworkError()
                is NetworkResponse.InputError -> NetworkResponse.InputError()
                is NetworkResponse.Success -> {
                    with(tokenResponse.data) {
                        val response = getUserByAccessToken(id, accessToken, this.refreshToken)
                        if (response is NetworkResponse.Success<User>) {
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
        val user = con.data.user.toUser(con.data.accessToken, con.data.refreshToken)
        cachedUser = user
        NetworkResponse.Success(user)
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
        val user = con.data.user.toUser(accessToken, refreshToken)
        cachedUser = user
        NetworkResponse.Success(user)
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
            NetworkResponse.Success(con.data.user.toUser(accessToken, refreshToken))
        }

    private suspend fun refreshToken(refreshToken: String): NetworkResponse<ResponseTokens.Data> =
        withContext(Dispatchers.IO) {
            val body: String
            try {
                body = service.refreshToken(refreshToken).string()
            } catch (e: Exception) {
                return@withContext processError<ResponseTokens.Data>(e)
            }

            val con = parseBody<ResponseTokens>(body)
            NetworkResponse.Success(
                ResponseTokens.Data(
                    con.data.accessToken,
                    con.data.refreshToken
                )
            )
        }
}