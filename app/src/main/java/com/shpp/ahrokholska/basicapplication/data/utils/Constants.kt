package com.shpp.ahrokholska.basicapplication.data.utils

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object Constants {
    val STORED_USER_NAME_KEY = stringPreferencesKey("STORED_USER_NAME_KEY")
    val IS_AUTO_LOGIN_ENABLED_KEY = booleanPreferencesKey("IS_AUTO_LOGIN_ENABLED")
}