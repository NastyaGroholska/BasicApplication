package com.shpp.ahrokholska.basicapplication.domain.repository.contactsRepository

import com.shpp.ahrokholska.basicapplication.domain.model.Contact
import kotlinx.coroutines.flow.StateFlow

interface ContactsRepository {
    val contacts: StateFlow<List<Contact>>

    suspend fun getContactWithId(id: Long): Contact?
    suspend fun addContact(name: String, career: String)
    suspend fun removeWithId(id: Long)
    suspend fun insertAt(contact: Contact, position: Int)
}