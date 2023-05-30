package com.shpp.ahrokholska.basicapplication.presentation.ui.loginGraph

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
class LoginGraphViewModel @Inject constructor(private val rep: UserRepository) : ViewModel() {
    private var _networkResponse = MutableSharedFlow<NetworkResponse<User>>()
    val networkResponse: SharedFlow<NetworkResponse<User>> = _networkResponse
    private var isProcessing = false

    fun authorizeUser(email: String, password: String) {
        if (isProcessing) return

        viewModelScope.launch {
            isProcessing = true
            _networkResponse.emit(rep.getUser(email, password))
            isProcessing = false
        }
    }

    fun authorizeAndSaveUser(email: String, password: String) {
        if (isProcessing) return

        viewModelScope.launch {
            isProcessing = true
            val response = rep.getUser(email, password)
            if (response.code == NetworkResponseCode.Success) {
                with(response.data) {
                    rep.saveUser(this?.id ?: 0, this?.refreshToken.orEmpty())
                }
            }
            _networkResponse.emit(response)
            isProcessing = false
        }
    }
}