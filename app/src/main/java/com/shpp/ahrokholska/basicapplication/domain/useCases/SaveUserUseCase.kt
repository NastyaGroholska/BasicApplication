package com.shpp.ahrokholska.basicapplication.domain.useCases

import com.shpp.ahrokholska.basicapplication.domain.repository.userRepository.UserLocalRepository
import javax.inject.Inject

class SaveUserUseCase @Inject constructor(private val localRepo: UserLocalRepository) {
    suspend operator fun invoke(id: Long, refreshToken: String) =
        localRepo.saveUser(id, refreshToken)
}