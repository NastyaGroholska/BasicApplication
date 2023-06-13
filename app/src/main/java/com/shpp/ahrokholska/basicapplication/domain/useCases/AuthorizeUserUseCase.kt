package com.shpp.ahrokholska.basicapplication.domain.useCases

import com.shpp.ahrokholska.basicapplication.domain.model.NetworkResponse
import com.shpp.ahrokholska.basicapplication.domain.model.User
import com.shpp.ahrokholska.basicapplication.domain.repository.userRepository.UserNetworkRepository
import javax.inject.Inject

class AuthorizeUserUseCase @Inject constructor(private val networkRepo: UserNetworkRepository) {

    suspend operator fun invoke(email: String, password: String): NetworkResponse<User> =
        networkRepo.authorizeUser(email, password)
}