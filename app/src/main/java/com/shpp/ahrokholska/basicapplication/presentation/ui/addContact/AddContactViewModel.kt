package com.shpp.ahrokholska.basicapplication.presentation.ui.addContact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shpp.ahrokholska.basicapplication.data.repository.HardcodedContactsRepositoryImpl
import com.shpp.ahrokholska.basicapplication.domain.repository.ContactsRepository
import kotlinx.coroutines.launch

class AddContactViewModel : ViewModel() {
    private val contactsRepository: ContactsRepository = HardcodedContactsRepositoryImpl()

    fun addContact(name: String, career: String) {
        viewModelScope.launch {
            contactsRepository.addContact(name, career)
        }
    }
}
