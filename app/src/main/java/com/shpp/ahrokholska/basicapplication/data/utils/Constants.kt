package com.shpp.ahrokholska.basicapplication.data.utils

import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object Constants {
    val REFRESH_TOKEN = stringPreferencesKey("REFRESH_TOKEN")
    val USER_ID = longPreferencesKey("USER_ID")
    const val AUTHORIZATION_HEADER = "Bearer "

    enum class RESPONSE_ERRORS(val code: Int) {
        UNAUTHORISED(401),
        INVALID_REQUEST(400),
        ACCESS_DENIED(403),
        NOT_FOUND(404)
    }
}