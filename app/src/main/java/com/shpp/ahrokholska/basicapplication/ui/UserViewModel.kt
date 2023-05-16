package com.shpp.ahrokholska.basicapplication.ui

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.shpp.ahrokholska.basicapplication.utils.ext.dataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val dataStore: DataStore<Preferences> = application.applicationContext.dataStore

    fun readStringFromStore(key: Preferences.Key<String>): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[key] ?: ""
        }
    }

    fun writeStringToStore(key: Preferences.Key<String>, value: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.edit { settings ->
                settings[key] = value
            }
        }
    }
}