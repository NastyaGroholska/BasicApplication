package com.shpp.ahrokholska.basicapplication.ui.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shpp.ahrokholska.basicapplication.data.Contact
import com.shpp.ahrokholska.basicapplication.data.ContactsDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ContactsViewModel : ViewModel() {
    private val contactsDB = ContactsDB()
    val contacts: StateFlow<List<Contact>> = contactsDB.contacts

    init {
        viewModelScope.launch(Dispatchers.IO) {
            contactsDB.initializeDB()
        }
    }

    fun deleteContact(contact: Contact) {
        contactsDB.removeWithId(contact.id)
    }

    fun insertContact(contact: Contact, position: Int) {
        contactsDB.insertAt(contact, position)
    }
}