package com.shpp.ahrokholska.basicapplication.data.repositories

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.shpp.ahrokholska.basicapplication.data.utils.Constants
import com.shpp.ahrokholska.basicapplication.data.utils.ext.dataStore
import com.shpp.ahrokholska.basicapplication.domain.repository.userRepository.UserLocalRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserLocalRepositoryImpl @Inject constructor(@ApplicationContext private val context: Context) :
    UserLocalRepository {
    override val savedToken: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[Constants.REFRESH_TOKEN].orEmpty()
    }.flowOn(Dispatchers.IO)

    override val savedId: Flow<Long> = context.dataStore.data.map { preferences ->
        preferences[Constants.USER_ID]?:0
    }.flowOn(Dispatchers.IO)

    override suspend fun saveUser(id: Long, refreshToken: String) {
        withContext(Dispatchers.IO) {
            context.dataStore.edit { settings ->
                settings[Constants.REFRESH_TOKEN] = refreshToken
                settings[Constants.USER_ID] = id
            }
        }
    }
}