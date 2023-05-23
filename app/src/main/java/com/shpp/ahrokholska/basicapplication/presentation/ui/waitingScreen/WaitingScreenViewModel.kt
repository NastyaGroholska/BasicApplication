package com.shpp.ahrokholska.basicapplication.presentation.ui.waitingScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.shpp.ahrokholska.basicapplication.data.repository.UserRepositoryImpl
import com.shpp.ahrokholska.basicapplication.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class WaitingScreenViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository: UserRepository = UserRepositoryImpl(application.applicationContext)

    val isAutoLoginEnabled: Flow<Boolean> = userRepository.isAutoLoginEnabled
}