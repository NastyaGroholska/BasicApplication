package com.shpp.ahrokholska.basicapplication.presentation.ui.addContact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shpp.ahrokholska.basicapplication.domain.repository.contactsRepository.ContactsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddContactViewModel @Inject constructor(private val contactsRepository: ContactsRepository) :
    ViewModel() {
    fun addContact(name: String, career: String) {
        viewModelScope.launch {
            contactsRepository.addContact(name, career)
        }
    }
}
