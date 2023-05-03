package com.shpp.ahrokholska.basicapplication.utils.ext

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "PreferenceDataStore")

fun Context.readStringFromStore(key: Preferences.Key<String>): Flow<String> {
    return dataStore.data.map { preferences ->
        preferences[key] ?: ""
    }
}

suspend fun Context.writeStringToStore(key: Preferences.Key<String>, value: String) {
    dataStore.edit { settings ->
        settings[key] = value
    }
}
