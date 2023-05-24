package com.shpp.ahrokholska.basicapplication.presentation.ui.myContacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shpp.ahrokholska.basicapplication.domain.model.Contact
import com.shpp.ahrokholska.basicapplication.data.repository.HardcodedContactsRepositoryImpl
import com.shpp.ahrokholska.basicapplication.domain.repository.ContactsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
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
}