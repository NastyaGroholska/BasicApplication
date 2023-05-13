package com.shpp.ahrokholska.basicapplication.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ContactsDB {
    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts: StateFlow<List<Contact>> = _contacts

    private val picURL = "https://www.petful.com/wp-content/uploads/2016/07/british-shorthair.jpg"

    fun initializeDB() {
        _contacts.value = (1..10).map { i ->
            Contact(picURL, "Person $i", "Unique career")
        }
    }


    fun addContact(name: String, career: String) {
        _contacts.value = _contacts.value.toMutableList().apply {
            add(Contact(picURL, name, career))
        }
    }

    fun removeWithId(id: Long) {
        _contacts.value = _contacts.value.toMutableList().apply { removeIf { it.id == id } }
    }

    fun insertAt(contact: Contact, position: Int) {
        _contacts.value = _contacts.value.toMutableList().apply { add(position, contact) }
    }
}