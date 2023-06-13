package com.shpp.ahrokholska.basicapplication.domain.useCases

import com.shpp.ahrokholska.basicapplication.domain.model.Contact
import com.shpp.ahrokholska.basicapplication.domain.model.NetworkResponse
import com.shpp.ahrokholska.basicapplication.domain.repository.contactsRepository.ContactsRepository
import javax.inject.Inject

class GetAllUsersUseCase @Inject constructor(private val contactsRepo: ContactsRepository) {

    suspend operator fun invoke(userId: Long, accessToken: String): NetworkResponse<List<Contact>> {
        val users = contactsRepo.getAllUsers(accessToken)

        if (users !is NetworkResponse.Success) return users

        return NetworkResponse.Success(filterUsers(userId, users.data))
    }

    private fun filterUsers(userId: Long, users: List<Contact>) =
        users.filter { it.id != userId && !it.name.isNullOrBlank() }

}