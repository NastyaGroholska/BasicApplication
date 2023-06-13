package com.shpp.ahrokholska.basicapplication.presentation.ui.waitingScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shpp.ahrokholska.basicapplication.domain.model.NetworkResponse
import com.shpp.ahrokholska.basicapplication.domain.useCases.GetSavedUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WaitingScreenViewModel @Inject constructor(getSavedUserUseCase: GetSavedUserUseCase) :
    ViewModel() {
    private var _isAutoLoginEnabled = MutableStateFlow<Boolean?>(null)
    val isAutoLoginEnabled: StateFlow<Boolean?> = _isAutoLoginEnabled

    init {
        viewModelScope.launch {
            while (isActive) {
                val response = getSavedUserUseCase()
                if (response == null || response is NetworkResponse.InputError) {
                    _isAutoLoginEnabled.value = false
                    break
                }

                if (response is NetworkResponse.Success) {
                    _isAutoLoginEnabled.value = true
                    break
                }
            }
        }
    }
}