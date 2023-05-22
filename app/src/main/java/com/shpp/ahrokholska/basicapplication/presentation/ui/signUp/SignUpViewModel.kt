package com.shpp.ahrokholska.basicapplication.presentation.ui.signUp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.shpp.ahrokholska.basicapplication.data.repository.UserRepositoryImpl
import com.shpp.ahrokholska.basicapplication.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository = UserRepositoryImpl(application.applicationContext)

    fun saveUsername(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.saveUsername(username)
        }
    }

    fun saveIsAutoLoginEnabled(isAutoLoginEnabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.saveAutoLoginState(isAutoLoginEnabled)
        }
    }
}