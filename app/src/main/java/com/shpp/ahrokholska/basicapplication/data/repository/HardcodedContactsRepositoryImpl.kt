package com.shpp.ahrokholska.basicapplication.data.repository

import com.shpp.ahrokholska.basicapplication.data.db.ContactsDB
import com.shpp.ahrokholska.basicapplication.domain.model.Contact
import com.shpp.ahrokholska.basicapplication.domain.repository.ContactsRepository
import kotlinx.coroutines.flow.StateFlow

class HardcodedContactsRepositoryImpl : ContactsRepository {

    override val contacts: StateFlow<List<Contact>> = ContactsDB.contacts

    override suspend fun getContactWithId(id: Long): Contact? {
        return ContactsDB.getContactWithId(id)
    }

    override suspend fun addContact(name: String, career: String) {
        return ContactsDB.addContact(name, career)
    }

    override suspend fun removeWithId(id: Long) {
        return ContactsDB.removeWithId(id)
    }

    override suspend fun insertAt(contact: Contact, position: Int) {
        return ContactsDB.insertAt(contact, position)
    }
}