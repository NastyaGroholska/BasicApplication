package com.shpp.ahrokholska.basicapplication.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.shpp.ahrokholska.basicapplication.domain.repository.userRepository.UserRepository
import com.shpp.ahrokholska.basicapplication.data.utils.Constants
import com.shpp.ahrokholska.basicapplication.data.utils.ext.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(@ApplicationContext private val context: Context) :
    UserRepository {
    override val userName: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[Constants.STORED_USER_NAME_KEY].orEmpty()
    }.flowOn(Dispatchers.IO)

    override val isAutoLoginEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[Constants.IS_AUTO_LOGIN_ENABLED_KEY] ?: false
    }.flowOn(Dispatchers.IO)

    override suspend fun saveUsername(value: String) {
        withContext(Dispatchers.IO) {
            context.dataStore.edit { settings ->
                settings[Constants.STORED_USER_NAME_KEY] = value
            }
        }
    }

    override suspend fun saveAutoLoginState(isAutoLoginEnabled: Boolean) {
        withContext(Dispatchers.IO) {
            context.dataStore.edit { settings ->
                settings[Constants.IS_AUTO_LOGIN_ENABLED_KEY] = isAutoLoginEnabled
            }
        }
    }
}