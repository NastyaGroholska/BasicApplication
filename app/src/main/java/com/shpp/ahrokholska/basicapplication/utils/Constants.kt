package com.shpp.ahrokholska.basicapplication.utils

import androidx.datastore.preferences.core.stringPreferencesKey

object Constants {
    const val USER_NAME = "com.shpp.ahrokholska.basic.UserName"
    val STORED_EMAIL_KEY =
        stringPreferencesKey("com.shpp.ahrokholska.basic.PreferenceDataStoreHelper.Email")
}