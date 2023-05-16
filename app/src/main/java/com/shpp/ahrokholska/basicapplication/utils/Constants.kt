package com.shpp.ahrokholska.basicapplication.utils

import androidx.datastore.preferences.core.stringPreferencesKey

object Constants {
    const val TRANSITION_NAME_IMAGE = "com.shpp.ahrokholska.basic.imageTransition"
    const val TRANSITION_NAME_USER_NAME = "com.shpp.ahrokholska.basic.userNameTransition"
    const val TRANSITION_NAME_CAREER = "com.shpp.ahrokholska.basic.careerTransition"
    val STORED_EMAIL_KEY =
        stringPreferencesKey("com.shpp.ahrokholska.basic.PreferenceDataStoreHelper.Email")
    val STORED_USER_NAME_KEY =
        stringPreferencesKey("com.shpp.ahrokholska.basic.PreferenceDataStoreHelper.UserName")
}