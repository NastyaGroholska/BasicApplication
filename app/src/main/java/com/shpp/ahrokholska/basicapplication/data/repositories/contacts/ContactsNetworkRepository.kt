package com.shpp.ahrokholska.basicapplication.data.repositories.contacts

import com.shpp.ahrokholska.basicapplication.data.utils.Constants.AUTHORIZATION_HEADER
import com.shpp.ahrokholska.basicapplication.data.utils.ErrorHandler
import com.shpp.ahrokholska.basicapplication.data.utils.Parser
import com.shpp.ahrokholska.basicapplication.domain.model.Contact
import com.shpp.ahrokholska.basicapplication.domain.model.NetworkResponse
import com.shpp.ahrokholska.basicapplication.domain.model.SuccessNetworkResponse
import com.shpp.ahrokholska.basicapplication.domain.repository.contactsRepository.ContactsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ContactsNetworkRepository @Inject constructor(private val service: ContactsNetworkService) :
    ContactsRepository {
    private var cachedContacts: List<Contact>? = null

    override fun getCachedContacts(): List<Contact>? = cachedContacts

    override suspend fun getContacts(userId: Long, accessToken: String) =
        withContext(Dispatchers.IO) {
            val body: String
            try {
                body = service.getUserContacts(userId, AUTHORIZATION_HEADER + accessToken).string()
            } catch (e: Exception) {
                return@withContext ErrorHandler.processError(e)
            }

            val con = Parser.parseBody<ResponseContacts>(body)

            val contacts = con.data.contacts.map {
                Contact(it.id, it.name, it.career, it.address)
            }
            cachedContacts = contacts
            SuccessNetworkResponse(contacts)
        }

    override suspend fun getAllUsers(accessToken: String): NetworkResponse<List<Contact>> =
        withContext(Dispatchers.IO) {
            val body: String
            try {
                body = service.getAllUsers(AUTHORIZATION_HEADER + accessToken).string()
            } catch (e: Exception) {
                return@withContext ErrorHandler.processError(e)
            }

            val con = Parser.parseBody<ResponseUsers>(body)
            val users = con.data.users.map {
                Contact(it.id, it.name, it.career, it.address)
            }
            SuccessNetworkResponse(users)
        }

    override suspend fun addContact(userId: Long, accessToken: String, contactId: Long) =
        withContext(Dispatchers.IO) {
            val body: String
            try {
                body = service.addContact(userId, AUTHORIZATION_HEADER + accessToken, contactId)
                    .string()
            } catch (e: Exception) {
                return@withContext ErrorHandler.processError(e)
            }

            val con = Parser.parseBody<ResponseContacts>(body)

            val contacts = con.data.contacts.map {
                Contact(it.id, it.name, it.career, it.address)
            }
            cachedContacts = contacts
            SuccessNetworkResponse(contacts)
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

        val con = Parser.parseBody<ResponseContacts>(body)

        val contacts = con.data.contacts.map {
            Contact(it.id, it.name, it.career, it.address)
        }
        cachedContacts = contacts
        SuccessNetworkResponse(contacts)
    }
}