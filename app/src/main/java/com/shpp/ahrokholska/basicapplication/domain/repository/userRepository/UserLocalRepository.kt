package com.shpp.ahrokholska.basicapplication.domain.repository.userRepository

import kotlinx.coroutines.flow.Flow

interface UserLocalRepository {
    val savedToken: Flow<String>
    val savedId: Flow<Long>
    suspend fun saveUser(id: Long, refreshToken: String)
}