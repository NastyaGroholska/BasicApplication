package com.shpp.ahrokholska.basicapplication.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.shpp.ahrokholska.basicapplication.data.Contact
import com.shpp.ahrokholska.basicapplication.data.ContactsDB
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ContactsViewModel : ViewModel() {
    private val contactsDB = ContactsDB
    val contacts: StateFlow<List<Contact>> = contactsDB.contacts
    private val _multiselectState = MutableStateFlow(false)
    val multiselectState: StateFlow<Boolean> = _multiselectState
    private val _selectedPositions = MutableStateFlow<List<Int>>(emptyList())
    val selectedPositions: StateFlow<List<Int>> = _selectedPositions

    private fun updateMultiselectState(newState: Boolean) {
        _multiselectState.value = newState
    }

    fun clearSelectedPositions() {
        _selectedPositions.value = emptyList()
        updateMultiselectState(false)
    }

    fun addSelectedPosition(position: Int) {
        if (_selectedPositions.value.isEmpty()) {
            updateMultiselectState(true)
        }
        _selectedPositions.value =
            _selectedPositions.value.toMutableList().apply { add(position) }.toList()
    }

    fun removeSelectedPosition(position: Int) {
        _selectedPositions.value =
            _selectedPositions.value.toMutableList().apply { removeIf { it == position } }.toList()
        if (_selectedPositions.value.isEmpty()) {
            updateMultiselectState(false)
        }
    }

    fun getContactWithId(id: Long): Contact {
        return contactsDB.getContactWithId(id)
    }

    fun deleteContact(contact: Contact) {
        contactsDB.removeWithId(contact.id)
    }

    fun addContact(name: String, career: String) {
        contactsDB.addContact(name, career)
    }

    fun insertContact(contact: Contact, position: Int) {
        contactsDB.insertAt(contact, position)
    }
}