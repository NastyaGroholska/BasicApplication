package com.shpp.ahrokholska.basicapplication.data.repositories.contacts

import com.shpp.ahrokholska.basicapplication.data.model.ResponseContacts
import com.shpp.ahrokholska.basicapplication.data.model.ResponseUsers
import com.shpp.ahrokholska.basicapplication.data.utils.Constants.AUTHORIZATION_HEADER
import com.shpp.ahrokholska.basicapplication.data.utils.ErrorHandler
import com.shpp.ahrokholska.basicapplication.data.utils.Parser
import com.shpp.ahrokholska.basicapplication.domain.model.Contact
import com.shpp.ahrokholska.basicapplication.domain.model.NetworkResponse
import com.shpp.ahrokholska.basicapplication.domain.repository.contactsRepository.ContactsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ContactsNetworkRepository @Inject constructor(private val service: ContactsNetworkService) :
    ContactsRepository {
    private var cachedContacts: List<Contact>? = null

    override fun getCachedContacts(): List<Contact>? = cachedContacts

    override suspend fun getContacts(
        userId: Long, accessToken: String
    ): NetworkResponse<List<Contact>> =
        withContext(Dispatchers.IO) {
            val body: String
            try {
                body = service.getUserContacts(userId, AUTHORIZATION_HEADER + accessToken).string()
            } catch (e: Exception) {
                return@withContext ErrorHandler.processError(e)
            }

            val response = Parser.parseBody<ResponseContacts>(body)
            val contacts = response.data.contacts.map { it.toContact() }
            cachedContacts = contacts
            NetworkResponse.Success(contacts)
        }

    override suspend fun getAllUsers(accessToken: String): NetworkResponse<List<Contact>> =
        withContext(Dispatchers.IO) {
            val body: String
            try {
                body = service.getAllUsers(AUTHORIZATION_HEADER + accessToken).string()
            } catch (e: Exception) {
                return@withContext ErrorHandler.processError(e)
            }

            val response = Parser.parseBody<ResponseUsers>(body)
            val users = response.data.users.map { it.toContact() }
            NetworkResponse.Success(users)
        }

    override suspend fun addContact(
        userId: Long, accessToken: String, contactId: Long
    ): NetworkResponse<List<Contact>> =
        withContext(Dispatchers.IO) {
            val body: String
            try {
                body = service.addContact(userId, AUTHORIZATION_HEADER + accessToken, contactId)
                    .string()
            } catch (e: Exception) {
                return@withContext ErrorHandler.processError(e)
            }

            val response = Parser.parseBody<ResponseContacts>(body)
            val contacts = response.data.contacts.map { it.toContact() }
            cachedContacts = contacts
            NetworkResponse.Success(contacts)
        }

    override suspend fun deleteContact(
        userId: Long, contactId: Long, accessToken: String
    ): NetworkResponse<List<Contact>> = withContext(Dispatchers.IO) {
        val body: String
        try {
            body = service.deleteContact(userId, contactId, AUTHORIZATION_HEADER + accessToken)
                .string()
        } catch (e: Exception) {
            return@withContext ErrorHandler.processError(e)
        }

        val response = Parser.parseBody<ResponseContacts>(body)
        val contacts = response.data.contacts.map { it.toContact() }
        cachedContacts = contacts
        NetworkResponse.Success(contacts)
    }
}