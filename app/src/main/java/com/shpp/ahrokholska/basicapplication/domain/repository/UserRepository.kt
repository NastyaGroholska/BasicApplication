package com.shpp.ahrokholska.basicapplication.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val userName: Flow<String>
    val isAutoLoginEnabled: Flow<Boolean>

    suspend fun saveUsername(value: String)
    suspend fun saveAutoLoginState(isAutoLoginEnabled: Boolean)
}