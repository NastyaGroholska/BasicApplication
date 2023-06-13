package com.shpp.ahrokholska.basicapplication.domain.useCases

import com.shpp.ahrokholska.basicapplication.domain.model.NetworkResponse
import com.shpp.ahrokholska.basicapplication.domain.model.User
import com.shpp.ahrokholska.basicapplication.domain.repository.userRepository.UserLocalRepository
import com.shpp.ahrokholska.basicapplication.domain.repository.userRepository.UserNetworkRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetSavedUserUseCase @Inject constructor(
    private val networkRepo: UserNetworkRepository, private val localRepo: UserLocalRepository
) {

    suspend operator fun invoke(): NetworkResponse<User>? {
        val token = localRepo.savedToken.first()
        val response = if (token.isEmpty()) {
            null
        } else {
            networkRepo.getUser(localRepo.savedId.first(), token)
        }
        return response
    }
}