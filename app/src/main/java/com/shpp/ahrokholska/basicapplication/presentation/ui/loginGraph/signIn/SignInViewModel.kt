package com.shpp.ahrokholska.basicapplication.presentation.ui.loginGraph.signIn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shpp.ahrokholska.basicapplication.domain.model.NetworkResponse
import com.shpp.ahrokholska.basicapplication.domain.model.NetworkResponseCode
import com.shpp.ahrokholska.basicapplication.domain.model.User
import com.shpp.ahrokholska.basicapplication.domain.repository.userRepository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(private val rep: UserRepository) : ViewModel() {
    private var _networkResponse = MutableSharedFlow<NetworkResponse<User>>()
    val networkResponse: SharedFlow<NetworkResponse<User>> = _networkResponse
    private var isProcessing = false

    fun authorizeUser(email: String, password: String) {
        if (isProcessing) return

        viewModelScope.launch {
            isProcessing = true
            _networkResponse.emit(rep.getSavedUser(email, password))
            isProcessing = false
        }
    }

    fun authorizeAndSaveUser(email: String, password: String) {
        if (isProcessing) return

        viewModelScope.launch {
            isProcessing = true
            val response = rep.getSavedUser(email, password)
            if (response.code == NetworkResponseCode.Success) {
                rep.saveUser(response.data.id, response.data.refreshToken)
            }
            _networkResponse.emit(response)
            isProcessing = false
        }
    }
}