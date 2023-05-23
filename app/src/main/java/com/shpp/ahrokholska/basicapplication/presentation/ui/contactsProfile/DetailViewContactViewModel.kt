package com.shpp.ahrokholska.basicapplication.presentation.ui.contactsProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shpp.ahrokholska.basicapplication.domain.model.Contact
import com.shpp.ahrokholska.basicapplication.data.repository.HardcodedContactsRepositoryImpl
import com.shpp.ahrokholska.basicapplication.domain.repository.ContactsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewContactViewModel : ViewModel() {
    private val contactsRepository: ContactsRepository = HardcodedContactsRepositoryImpl()

    private val _contact = MutableStateFlow<Contact?>(null)
    val contact: StateFlow<Contact?> = _contact

    fun getContactWithId(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _contact.emit(contactsRepository.getContactWithId(id))
        }
    }
}