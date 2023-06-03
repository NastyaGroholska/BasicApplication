package com.shpp.ahrokholska.basicapplication.domain.useCases

import com.shpp.ahrokholska.basicapplication.domain.model.Contact
import com.shpp.ahrokholska.basicapplication.domain.model.NetworkResponse
import com.shpp.ahrokholska.basicapplication.domain.repository.contactsRepository.ContactsRepository
import javax.inject.Inject

class AddContactUseCase @Inject constructor(private val contactsRepo: ContactsRepository) {
    suspend operator fun invoke(
        userId: Long, accessToken: String, contactId: Long
    ): NetworkResponse<List<Contact>> = contactsRepo.addContact(userId, accessToken, contactId)
}