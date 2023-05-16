package com.shpp.ahrokholska.basicapplication.utils

import androidx.datastore.preferences.core.stringPreferencesKey

object Constants {
    val STORED_EMAIL_KEY =
        stringPreferencesKey("com.shpp.ahrokholska.basic.PreferenceDataStoreHelper.Email")
    val STORED_USER_NAME_KEY =
        stringPreferencesKey("com.shpp.ahrokholska.basic.PreferenceDataStoreHelper.UserName")
}