package com.shpp.ahrokholska.basicapplication.ui.viewmodels

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.shpp.ahrokholska.basicapplication.utils.Constants
import com.shpp.ahrokholska.basicapplication.utils.ext.dataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val dataStore: DataStore<Preferences> = application.applicationContext.dataStore

    private val _userNameStateFlow = MutableStateFlow("")
    var userNameStateFlow: StateFlow<String> = _userNameStateFlow

    init {
        runBlocking {
            _userNameStateFlow.value = dataStore.data.map { preferences ->
                preferences[Constants.STORED_USER_NAME_KEY] ?: ""
            }.first()
        }
    }

    fun saveTmpUser(username: String) {
        _userNameStateFlow.value = username
    }


    fun writeStringToStore(key: Preferences.Key<String>, value: String) {
        _userNameStateFlow.value = value
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.edit { settings ->
                settings[key] = value
            }
        }
    }
}