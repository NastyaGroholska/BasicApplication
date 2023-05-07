package com.shpp.ahrokholska.basicapplication.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ContactsDB {
    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts: StateFlow<List<Contact>> = _contacts

    fun initializeDB() {
        _contacts.value = (1..10).map { i ->
            Contact(
                i, "https://www.petful.com/wp-content/uploads/2016/07/british-shorthair.jpg",
                "Person $i", "Unique career"
            )
        }
    }

    fun removeWithId(id: Int) {
        _contacts.update { value ->
            val mutableValue = value.toMutableList()
            mutableValue.removeIf { it.id == id }
            mutableValue
        }
    }

    fun insertAt(contact: Contact, position: Int) {
        _contacts.update { value ->
            val mutableValue = value.toMutableList()
            mutableValue.add(position, contact)
            mutableValue
        }
    }
}