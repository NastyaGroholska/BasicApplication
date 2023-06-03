package com.shpp.ahrokholska.basicapplication.presentation.ui.addContact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shpp.ahrokholska.basicapplication.domain.model.Contact
import com.shpp.ahrokholska.basicapplication.domain.model.SuccessNetworkResponse
import com.shpp.ahrokholska.basicapplication.domain.useCases.AddContactUseCase
import com.shpp.ahrokholska.basicapplication.domain.useCases.GetCachedUserUseCase
import com.shpp.ahrokholska.basicapplication.domain.useCases.GetPossibleContactsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    private lateinit var _states: Array<MutableStateFlow<State>>
    lateinit var states: Array<StateFlow<State>>
        private set
    private var _finishedAll = MutableStateFlow(true)
    val finishedAll: StateFlow<Boolean> = _finishedAll
    private var numberOfCoroutines = AtomicInteger()
    val isLoading: Boolean
        get() = numberOfCoroutines.get() > 0


    init {
        viewModelScope.launch {
            while (isActive) {
                val contacts = getPossibleContactsUseCase(user.id, user.accessToken)
                if (contacts is SuccessNetworkResponse) {
                    _states = Array(contacts.data.size) { MutableStateFlow(State.Normal) }
                    states = Array(contacts.data.size) { _states[it] }
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
            _states[contactPos].emit(State.Loading)

            if (addContactUseCase(user.id, user.accessToken, contactId) is SuccessNetworkResponse) {
                _states[contactPos].emit(State.Loaded)
            } else {
                _states[contactPos].emit(State.Failed)
            }

            if (numberOfCoroutines.decrementAndGet() == 0) {
                _finishedAll.emit(true)
            }
        }
    }
}
