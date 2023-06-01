package com.shpp.ahrokholska.basicapplication.domain.useCases

import com.shpp.ahrokholska.basicapplication.domain.model.NetworkResponse
import com.shpp.ahrokholska.basicapplication.domain.model.User
import com.shpp.ahrokholska.basicapplication.domain.repository.userRepository.UserNetworkRepository
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(private val networkRepo: UserNetworkRepository) {
    suspend operator fun invoke(
        email: String, password: String, name: String?, phone: String?
    ): NetworkResponse<User> = networkRepo.createUser(email, password, name, phone)
}