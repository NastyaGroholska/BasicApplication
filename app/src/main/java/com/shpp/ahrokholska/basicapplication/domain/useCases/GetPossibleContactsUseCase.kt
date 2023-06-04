package com.shpp.ahrokholska.basicapplication.domain.useCases

import com.shpp.ahrokholska.basicapplication.domain.model.Contact
import com.shpp.ahrokholska.basicapplication.domain.model.NetworkResponse
import javax.inject.Inject

class GetPossibleContactsUseCase @Inject constructor(
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val getContactsUseCase: GetContactsUseCase
) {
    suspend operator fun invoke(userId: Long, accessToken: String): NetworkResponse<List<Contact>> {
        val users = getAllUsersUseCase(userId, accessToken)
        if (users !is NetworkResponse.Success) return users

        val contacts = getContactsUseCase(userId, accessToken)
        if (contacts !is NetworkResponse.Success) return contacts

        return NetworkResponse.Success(users.data.filter { !contacts.data.contains(it) })
    }
}