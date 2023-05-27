package com.shpp.ahrokholska.basicapplication.data.repository

import com.shpp.ahrokholska.basicapplication.data.db.ContactsDB
import com.shpp.ahrokholska.basicapplication.domain.model.Contact
import com.shpp.ahrokholska.basicapplication.domain.repository.contactsRepository.ContactsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class HardcodedContactsRepositoryImpl : ContactsRepository {
    override val contacts: StateFlow<List<Contact>> = ContactsDB.contacts

    override suspend fun getContactWithId(id: Long): Contact? {
        return withContext(Dispatchers.IO) {
            ContactsDB.getContactWithId(id)
        }
    }

    override suspend fun addContact(name: String, career: String) {
        withContext(Dispatchers.IO) {
            ContactsDB.addContact(name, career)
        }
    }

    override suspend fun removeWithId(id: Long) {
        withContext(Dispatchers.IO) {
            ContactsDB.removeWithId(id)
        }
    }

    override suspend fun insertAt(contact: Contact, position: Int) {
        withContext(Dispatchers.IO) {
            ContactsDB.insertAt(contact, position)
        }
    }
}