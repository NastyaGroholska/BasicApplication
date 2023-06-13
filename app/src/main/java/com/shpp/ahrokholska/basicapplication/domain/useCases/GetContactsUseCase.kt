package com.shpp.ahrokholska.basicapplication.domain.useCases

import com.shpp.ahrokholska.basicapplication.domain.model.Contact
import com.shpp.ahrokholska.basicapplication.domain.model.NetworkResponse
import com.shpp.ahrokholska.basicapplication.domain.repository.contactsRepository.ContactsRepository
import javax.inject.Inject

class GetContactsUseCase @Inject constructor(private val contactsRepo: ContactsRepository) {

    suspend operator fun invoke(userId: Long, accessToken: String): NetworkResponse<List<Contact>> {
        contactsRepo.getCachedContacts()?.let {
            return NetworkResponse.Success(it)
        }

        return contactsRepo.getContacts(userId, accessToken)
    }
}