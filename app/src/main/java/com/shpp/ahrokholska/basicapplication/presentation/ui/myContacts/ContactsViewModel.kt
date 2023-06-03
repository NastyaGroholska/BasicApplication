package com.shpp.ahrokholska.basicapplication.presentation.ui.myContacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shpp.ahrokholska.basicapplication.domain.model.Contact
import com.shpp.ahrokholska.basicapplication.domain.model.SuccessNetworkResponse
import com.shpp.ahrokholska.basicapplication.domain.useCases.AddContactUseCase
import com.shpp.ahrokholska.basicapplication.domain.useCases.DeleteContactUseCase
import com.shpp.ahrokholska.basicapplication.domain.useCases.GetCachedUserUseCase
import com.shpp.ahrokholska.basicapplication.domain.useCases.GetContactsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
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

    fun updateContacts() {
        viewModelScope.launch {
            while (isActive) {
                val contacts = getContactsUseCase(user.id, user.accessToken)
                if (contacts is SuccessNetworkResponse) {
                    _contacts.value = contacts.data
                    break
                }
            }
        }
    }

    fun deleteContact(contact: Contact) {
        viewModelScope.launch {
            val response = deleteContactUseCase(user.id, user.accessToken, contact.id)
            if (response is SuccessNetworkResponse) {
                _contacts.value = response.data
            }
        }
    }

    fun insertContact(contact: Contact) {
        viewModelScope.launch {
            val response = addContactUseCase(user.id, user.accessToken, contact.id)
            if (response is SuccessNetworkResponse) {
                _contacts.value = response.data
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