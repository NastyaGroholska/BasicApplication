package com.shpp.ahrokholska.basicapplication.domain.useCases

import com.shpp.ahrokholska.basicapplication.domain.model.User
import com.shpp.ahrokholska.basicapplication.domain.repository.userRepository.UserNetworkRepository
import javax.inject.Inject

class GetCachedUserUseCase @Inject constructor(private val networkRepo: UserNetworkRepository) {
    operator fun invoke(): User = networkRepo.getCachedUser()!!
}