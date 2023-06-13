package com.shpp.ahrokholska.basicapplication.presentation.ui.myProfile

import androidx.lifecycle.ViewModel
import com.shpp.ahrokholska.basicapplication.domain.model.User
import com.shpp.ahrokholska.basicapplication.domain.useCases.GetCachedUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(private val getCachedUserUseCase: GetCachedUserUseCase) :
    ViewModel() {
    val userName: User get() = getCachedUserUseCase()
}