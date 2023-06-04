package com.shpp.ahrokholska.basicapplication.presentation.ui.addContact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shpp.ahrokholska.basicapplication.domain.model.Contact
import com.shpp.ahrokholska.basicapplication.domain.model.NetworkResponse
import com.shpp.ahrokholska.basicapplication.domain.useCases.AddContactUseCase
import com.shpp.ahrokholska.basicapplication.domain.useCases.GetCachedUserUseCase
import com.shpp.ahrokholska.basicapplication.domain.useCases.GetPossibleContactsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

@HiltViewModel
class AddContactViewModel @Inject constructor(
    private val getPossibleContactsUseCase: GetPossibleContactsUseCase,
    private val getCachedUserUseCase: GetCachedUserUseCase,
    private val addContactUseCase: AddContactUseCase
) :
    ViewModel() {
    private val user get() = getCachedUserUseCase()
    private var _contacts = MutableStateFlow<List<Contact>?>(null)
    val contacts: StateFlow<List<Contact>?> = _contacts
    private var _states: MutableStateFlow<Array<State>> = MutableStateFlow(emptyArray())
    val states: StateFlow<Array<State>> = _states
    private var _finishedAll = MutableStateFlow(true)
    val finishedAll: StateFlow<Boolean> = _finishedAll
    private var numberOfCoroutines = AtomicInteger()
    val isLoading: Boolean
        get() = numberOfCoroutines.get() > 0


    init {
        viewModelScope.launch {
            while (isActive) {
                val contacts = getPossibleContactsUseCase(user.id, user.accessToken)
                if (contacts is NetworkResponse.Success) {
                    _states.value = Array(contacts.data.size) { State.Normal }
                    _contacts.value = contacts.data
                    break
                }
            }
        }
    }

    fun addContact(contactPos: Int, contactId: Long) {
        numberOfCoroutines.getAndIncrement()
        viewModelScope.launch {
            _finishedAll.emit(false)
            _states.update { it.copyOf().apply { this[contactPos] = State.Loading } }

            if (addContactUseCase(
                    user.id,
                    user.accessToken,
                    contactId
                ) is NetworkResponse.Success
            ) {
                _states.update { it.copyOf().apply { this[contactPos] = State.Loaded } }
            } else {
                _states.update { it.copyOf().apply { this[contactPos] = State.Failed } }
            }

            if (numberOfCoroutines.decrementAndGet() == 0) {
                _finishedAll.emit(true)
            }
        }
    }
}
