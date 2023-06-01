package com.shpp.ahrokholska.basicapplication.domain.useCases

import com.shpp.ahrokholska.basicapplication.domain.model.NetworkResponse
import com.shpp.ahrokholska.basicapplication.domain.model.User
import com.shpp.ahrokholska.basicapplication.domain.repository.userRepository.UserNetworkRepository
import javax.inject.Inject

class EditUserUseCase @Inject constructor(private val networkRepo: UserNetworkRepository) {
    suspend operator fun invoke(
        id: Long, accessToken: String, refreshToken: String,
        name: String, career: String?, phone: String, address: String?, date: String?
    ): NetworkResponse<User> =
        networkRepo.editUser(id, accessToken, refreshToken, name, career, phone, address, date)
}