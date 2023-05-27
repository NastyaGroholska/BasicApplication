package com.shpp.ahrokholska.basicapplication.presentation.ui.signUp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shpp.ahrokholska.basicapplication.domain.repository.userRepository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    fun saveUsername(username: String) {
        viewModelScope.launch {
            userRepository.saveUsername(username)
        }
    }

    fun saveIsAutoLoginEnabled(isAutoLoginEnabled: Boolean) {
        viewModelScope.launch {
            userRepository.saveAutoLoginState(isAutoLoginEnabled)
        }
    }
}