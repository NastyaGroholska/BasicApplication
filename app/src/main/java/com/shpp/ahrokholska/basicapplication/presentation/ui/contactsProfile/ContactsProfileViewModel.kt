package com.shpp.ahrokholska.basicapplication.presentation.ui.contactsProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shpp.ahrokholska.basicapplication.domain.model.Contact
import com.shpp.ahrokholska.basicapplication.domain.repository.contactsRepository.ContactsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsProfileViewModel @Inject constructor(private val contactsRepository: ContactsRepository) :
    ViewModel() {
    private val _contact = MutableStateFlow<Contact?>(null)
    val contact: StateFlow<Contact?> = _contact

    fun getContactWithId(id: Long) {
        viewModelScope.launch {
            _contact.emit(contactsRepository.getContactWithId(id))
        }
    }
}