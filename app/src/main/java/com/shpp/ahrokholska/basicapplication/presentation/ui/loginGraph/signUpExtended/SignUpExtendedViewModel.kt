package com.shpp.ahrokholska.basicapplication.presentation.ui.loginGraph.signUpExtended

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
class SignUpExtendedViewModel @Inject constructor(private val rep: UserRepository) : ViewModel() {
    private var _networkResponse = MutableSharedFlow<NetworkResponse<User>>()
    val networkResponse: SharedFlow<NetworkResponse<User>> = _networkResponse
    private var isProcessing = false

    fun createUser(email: String, password: String, name: String?, phone: String?) {
        if (isProcessing) return

        viewModelScope.launch {
            isProcessing = true
            _networkResponse.emit(rep.createUser(email, password, name, phone))
            isProcessing = false
        }
    }

    fun createAndSaveUser(email: String, password: String, name: String?, phone: String?) {
        if (isProcessing) return

        viewModelScope.launch {
            isProcessing = true
            val response = rep.createUser(email, password, name, phone)
            if (response.code == NetworkResponseCode.Success) {
                rep.saveUser(response.data.id, response.data.refreshToken)
            }
            _networkResponse.emit(response)
            isProcessing = false
        }
    }
}