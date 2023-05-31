package com.shpp.ahrokholska.basicapplication.domain.repository.userRepository

import com.shpp.ahrokholska.basicapplication.domain.model.NetworkResponse
import com.shpp.ahrokholska.basicapplication.domain.model.NetworkResponseCode
import com.shpp.ahrokholska.basicapplication.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val networkRepo: UserNetworkRepository, private val localRepo: UserLocalRepository
) {
    private var _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    suspend fun getSavedUser(): NetworkResponse<User>? {
        val token = localRepo.savedToken.first()
        val response = if (token.isEmpty()) {
            null
        } else {
            networkRepo.getUser(localRepo.savedId.first(), token)
        }
        _user.value = response?.data
        return response
    }

    suspend fun getUser(email: String, password: String): NetworkResponse<User> {
        val response = networkRepo.authorizeUser(email, password)
        if (response.code == NetworkResponseCode.Success) {
            _user.value = response.data
        }
        return response
    }

    suspend fun saveUser(id: Long, refreshToken: String) {
        localRepo.saveUser(id, refreshToken)
    }

    suspend fun createUser(
        email: String, password: String, name: String?, phone: String?
    ): NetworkResponse<User> {
        val response = networkRepo.createUser(email, password, name, phone)
        if (response.code == NetworkResponseCode.Success) {
            _user.value = response.data
        }
        return response
    }

    suspend fun editUser(
        name: String, career: String?, phone: String, address: String?, date: String?
    ): NetworkResponse<User> {
        with(_user.value!!) {
            val response = networkRepo.editUser(
                id, accessToken, refreshToken,
                name, career, phone, address, date
            )

            if (response.code == NetworkResponseCode.Success) {
                _user.value = response.data
            }
            return response
        }
    }
}