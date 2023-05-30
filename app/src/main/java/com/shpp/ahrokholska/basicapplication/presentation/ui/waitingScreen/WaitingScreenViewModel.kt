package com.shpp.ahrokholska.basicapplication.presentation.ui.waitingScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shpp.ahrokholska.basicapplication.domain.model.NetworkResponseCode
import com.shpp.ahrokholska.basicapplication.domain.repository.userRepository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WaitingScreenViewModel @Inject constructor(userRepository: UserRepository) : ViewModel() {
    private var _isAutoLoginEnabled = MutableStateFlow<Boolean?>(null)
    val isAutoLoginEnabled: StateFlow<Boolean?> = _isAutoLoginEnabled

    init {
        viewModelScope.launch {
            while (true) {
                val response = userRepository.getUser()
                if (response == null || response.code == NetworkResponseCode.InputError) {
                    _isAutoLoginEnabled.value = false
                    break
                }

                if (response.code == NetworkResponseCode.Success) {
                    _isAutoLoginEnabled.value = true
                    break
                }
            }
        }
    }
}