package com.shpp.ahrokholska.basicapplication.presentation.ui.myProfile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.shpp.ahrokholska.basicapplication.data.repository.UserRepositoryImpl
import com.shpp.ahrokholska.basicapplication.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class MyProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository: UserRepository = UserRepositoryImpl(application.applicationContext)

    val userName: Flow<String> = userRepository.userName
}