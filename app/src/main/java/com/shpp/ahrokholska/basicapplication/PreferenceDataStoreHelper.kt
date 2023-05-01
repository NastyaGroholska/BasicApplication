package com.shpp.ahrokholska.basicapplication

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "PreferenceDataStore") // TODO extract string literal to const value

suspend fun Context.readStringFromStore(key: String): String {
    return dataStore.data.map { preferences ->
        preferences[stringPreferencesKey(key)] ?: ""
    }.first() //TODO maybe collect
}

suspend fun Context.writeStringToStore(key: String, value: String) {
    dataStore.edit { settings ->
        settings[stringPreferencesKey(key)] = value
    }
}

const val STORED_EMAIL_KEY = "com.shpp.ahrokholska.basic.PreferenceDataStoreHelper.Email"
