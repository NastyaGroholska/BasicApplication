package com.shpp.ahrokholska.basicapplication.domain.repository.userRepository

import com.shpp.ahrokholska.basicapplication.domain.model.NetworkResponse
import com.shpp.ahrokholska.basicapplication.domain.model.User

interface UserNetworkRepository {
    suspend fun authorizeUser(email: String, password: String): NetworkResponse<User>
    suspend fun getUser(id: Long, refreshToken: String): NetworkResponse<User>
    suspend fun createUser(
        email: String, password: String, name: String?, phone: String?
    ): NetworkResponse<User>

    suspend fun editUser(
        id: Long, accessToken: String, refreshToken: String,
        name: String, career: String?, phone: String, address: String?, date: String?
    ): NetworkResponse<User>
}