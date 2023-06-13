package com.shpp.ahrokholska.basicapplication.domain.repository.contactsRepository

import com.shpp.ahrokholska.basicapplication.domain.model.Contact
import com.shpp.ahrokholska.basicapplication.domain.model.NetworkResponse

interface ContactsRepository {
    fun getCachedContacts(): List<Contact>?
    suspend fun getContacts(userId: Long, accessToken: String): NetworkResponse<List<Contact>>
    suspend fun getAllUsers(accessToken: String): NetworkResponse<List<Contact>>
    suspend fun addContact(
        userId: Long, accessToken: String, contactId: Long
    ): NetworkResponse<List<Contact>>

    suspend fun deleteContact(
        userId: Long, contactId: Long, accessToken: String
    ): NetworkResponse<List<Contact>>
}