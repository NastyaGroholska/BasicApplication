package com.shpp.ahrokholska.basicapplication.presentation.ui.myContacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shpp.ahrokholska.basicapplication.domain.model.Contact
import com.shpp.ahrokholska.basicapplication.domain.model.NetworkResponse
import com.shpp.ahrokholska.basicapplication.domain.useCases.AddContactUseCase
import com.shpp.ahrokholska.basicapplication.domain.useCases.DeleteContactUseCase
import com.shpp.ahrokholska.basicapplication.domain.useCases.GetCachedUserUseCase
import com.shpp.ahrokholska.basicapplication.domain.useCases.GetContactsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val getContactsUseCase: GetContactsUseCase,
    private val deleteContactUseCase: DeleteContactUseCase,
    private val addContactUseCase: AddContactUseCase,
    getCachedUserUseCase: GetCachedUserUseCase
) : ViewModel() {
    private val user = getCachedUserUseCase()
    private var _contacts = MutableStateFlow<List<Contact>?>(null)
    val contacts: StateFlow<List<Contact>?> = _contacts
    private var _error = MutableStateFlow(false)
    val error: StateFlow<Boolean> = _error

    fun updateContacts() {
        viewModelScope.launch {
            val contacts = getContactsUseCase(user.id, user.accessToken)
            if (contacts is NetworkResponse.Success) {
                _contacts.value = contacts.data
                _error.emit(false)
            } else {
                setError()
            }
        }
    }

    private suspend fun setError() {
        _error.emit(true)
        _contacts.update { null }
    }

    fun deleteContact(contact: Contact) {
        viewModelScope.launch {
            val response = deleteContactUseCase(user.id, user.accessToken, contact.id)
            if (response is NetworkResponse.Success) {
                _contacts.update { response.data }
                _error.emit(false)
            } else {
                setError()
            }
        }
    }

    fun insertContact(contact: Contact) {
        viewModelScope.launch {
            val response = addContactUseCase(user.id, user.accessToken, contact.id)
            if (response is NetworkResponse.Success) {
                _contacts.value = response.data
                _error.emit(false)
            } else {
                setError()
            }
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