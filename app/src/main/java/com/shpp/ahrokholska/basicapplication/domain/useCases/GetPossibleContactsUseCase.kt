package com.shpp.ahrokholska.basicapplication.domain.useCases

import com.shpp.ahrokholska.basicapplication.domain.model.Contact
import com.shpp.ahrokholska.basicapplication.domain.model.NetworkResponse
import com.shpp.ahrokholska.basicapplication.domain.model.SuccessNetworkResponse
import javax.inject.Inject

class GetPossibleContactsUseCase @Inject constructor(
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val getContactsUseCase: GetContactsUseCase
) {
    suspend operator fun invoke(userId: Long, accessToken: String): NetworkResponse<List<Contact>> {
        val users = getAllUsersUseCase(userId, accessToken)
        if (users !is SuccessNetworkResponse) {
            return users
        }

        val contacts = getContactsUseCase(userId, accessToken)
        if (contacts !is SuccessNetworkResponse) {
            return contacts
        }

        return SuccessNetworkResponse(users.data.filter { !contacts.data.contains(it) })
    }
}