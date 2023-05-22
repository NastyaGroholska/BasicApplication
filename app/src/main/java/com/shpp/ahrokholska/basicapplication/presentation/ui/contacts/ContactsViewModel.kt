package com.shpp.ahrokholska.basicapplication.presentation.ui.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shpp.ahrokholska.basicapplication.domain.model.Contact
import com.shpp.ahrokholska.basicapplication.data.repository.HardcodedContactsRepositoryImpl
import com.shpp.ahrokholska.basicapplication.domain.repository.ContactsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ContactsViewModel : ViewModel() {
    private val contactsRepository: ContactsRepository = HardcodedContactsRepositoryImpl()

    val contacts: StateFlow<List<Contact>> = contactsRepository.contacts

    fun deleteContact(contact: Contact) {
        viewModelScope.launch(Dispatchers.IO) {
            contactsRepository.removeWithId(contact.id)
        }
    }

    fun insertContact(contact: Contact, position: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            contactsRepository.insertAt(contact, position)
        }
    }
}