package com.shpp.ahrokholska.basicapplication.presentation.ui.waitingScreen

import androidx.lifecycle.ViewModel
import com.shpp.ahrokholska.basicapplication.domain.repository.userRepository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class WaitingScreenViewModel @Inject constructor(userRepository: UserRepository) : ViewModel() {
    val isAutoLoginEnabled: Flow<Boolean> = userRepository.isAutoLoginEnabled
}