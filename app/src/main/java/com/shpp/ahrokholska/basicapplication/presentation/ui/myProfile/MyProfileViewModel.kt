package com.shpp.ahrokholska.basicapplication.presentation.ui.myProfile

import androidx.lifecycle.ViewModel
import com.shpp.ahrokholska.basicapplication.domain.repository.userRepository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(userRepository: UserRepository) : ViewModel() {
    val userName: Flow<String> = userRepository.userName
}