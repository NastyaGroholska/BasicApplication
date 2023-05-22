package com.shpp.ahrokholska.basicapplication.presentation.utils

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object Constants {
    const val TRANSITION_NAME_IMAGE = "com.shpp.ahrokholska.basic.imageTransition"
    const val TRANSITION_NAME_USER_NAME = "com.shpp.ahrokholska.basic.userNameTransition"
    const val TRANSITION_NAME_CAREER = "com.shpp.ahrokholska.basic.careerTransition"
    val STORED_USER_NAME_KEY =
        stringPreferencesKey("com.shpp.ahrokholska.basic.PreferenceDataStoreHelper.UserName")
    val IS_AUTO_LOGIN_ENABLED_KEY = booleanPreferencesKey("IS_AUTO_LOGIN_ENABLED")
}
// TODO think about it:
//    const val TRANSITION_NAME_IMAGE_KEY = "TRANSITION_NAME_IMAGE"
//    const val TRANSITION_NAME_USER_NAME_KEY = "TRANSITION_NAME_USER_NAME"
//    const val TRANSITION_NAME_CAREER_KEY = "TRANSITION_NAME_CAREER"
//    val STORED_USER_NAME_KEY =
//        stringPreferencesKey("PreferenceDataStoreHelper.UserName")
