package com.shpp.ahrokholska.basicapplication.presentation.ui.editProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shpp.ahrokholska.basicapplication.domain.model.NetworkResponse
import com.shpp.ahrokholska.basicapplication.domain.model.User
import com.shpp.ahrokholska.basicapplication.domain.useCases.EditUserUseCase
import com.shpp.ahrokholska.basicapplication.domain.useCases.GetCachedUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    getCachedUserUseCase: GetCachedUserUseCase, private val editUserUseCase: EditUserUseCase
) :
    ViewModel() {
    private var _networkResponse = MutableSharedFlow<NetworkResponse<User>>()
    val networkResponse: SharedFlow<NetworkResponse<User>> = _networkResponse
    var user: User = getCachedUserUseCase()
    private var isProcessing = false

    fun editUser(name: String, career: String?, phone: String, address: String?, date: String?) {
        if (isProcessing) return

        viewModelScope.launch {
            isProcessing = true
            _networkResponse.emit(
                editUserUseCase(
                    user.id, user.accessToken, user.refreshToken, name, career, phone, address, date
                )
            )
            isProcessing = false
        }
    }
}