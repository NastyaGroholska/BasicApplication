package com.shpp.ahrokholska.basicapplication.presentation.ui.contactsProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shpp.ahrokholska.basicapplication.domain.model.Contact
import com.shpp.ahrokholska.basicapplication.domain.model.SuccessNetworkResponse
import com.shpp.ahrokholska.basicapplication.domain.useCases.GetCachedUserUseCase
import com.shpp.ahrokholska.basicapplication.domain.useCases.GetContactsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsProfileViewModel @Inject constructor(
    private val getContactsUseCase: GetContactsUseCase, getCachedUserUseCase: GetCachedUserUseCase
) : ViewModel() {
    private val user = getCachedUserUseCase()
    private val _contact = MutableStateFlow<Contact?>(null)
    val contact: StateFlow<Contact?> = _contact

    fun getContactWithId(id: Long) {
        viewModelScope.launch {
            while (isActive) {
                val contacts = getContactsUseCase(user.id, user.accessToken)
                if (contacts is SuccessNetworkResponse) {
                    _contact.value = contacts.data.find { it.id == id }
                    break
                }
            }
        }
    }
}