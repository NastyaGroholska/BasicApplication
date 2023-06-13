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
    private var _cachedContacts = MutableStateFlow<List<Contact>?>(null)
    private var _contacts = MutableStateFlow<List<Contact>?>(null)
    val contacts: StateFlow<List<Contact>?> = _contacts
    private var _error = MutableStateFlow(false)
    val error: StateFlow<Boolean> = _error
    private var filter: String? = null

    fun updateContacts() {
        viewModelScope.launch {
            val contacts = getContactsUseCase(user.id, user.accessToken)
            if (contacts is NetworkResponse.Success) {
                _contacts.value = contacts.data
                _cachedContacts.value = _contacts.value
                _error.emit(false)
            } else {
                setError()
            }
        }
    }

    fun setFilter(filter: String?) {
        this.filter = filter
        setContacts(_cachedContacts.value)
    }

    private fun setContacts(contacts: List<Contact>?) {
        _cachedContacts.update { contacts }
        if (filter.isNullOrBlank()) {
            _contacts.update { contacts }
        } else {
            _contacts.update {
                contacts?.filter {
                    it.name?.contains(filter ?: "", ignoreCase = true) ?: false
                }
            }
        }
    }

    private suspend fun setError() {
        _error.emit(true)
        setContacts(null)
    }

    fun deleteContact(contact: Contact) {
        viewModelScope.launch {
            val response = deleteContactUseCase(user.id, user.accessToken, contact.id)
            if (response is NetworkResponse.Success) {
                setContacts(response.data)
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
                setContacts(response.data)
                _error.emit(false)
            } else {
                setError()
            }
        }
    }

    private val _multiselectState = MutableStateFlow(false)
    val multiselectState: StateFlow<Boolean> = _multiselectState
    private val _selectedIds = MutableStateFlow<List<Long>>(emptyList())
    val selectedIds: StateFlow<List<Long>> = _selectedIds

    private fun updateMultiselectState(newState: Boolean) {
        _multiselectState.value = newState
    }

    fun clearSelectedPositions() {
        _selectedIds.value = emptyList()
        updateMultiselectState(false)
    }

    fun addSelectedId(id: Long) {
        if (_selectedIds.value.isEmpty()) {
            updateMultiselectState(true)
        }

        _selectedIds.value = _selectedIds.value.toMutableList().apply { add(id) }.toList()
    }

    fun removeSelectedId(id: Long) {
        _selectedIds.value =
            _selectedIds.value.toMutableList().apply { removeIf { it == id } }.toList()
        if (_selectedIds.value.isEmpty()) {
            updateMultiselectState(false)
        }
    }
}