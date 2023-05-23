package com.shpp.ahrokholska.basicapplication.data.db

import com.shpp.ahrokholska.basicapplication.domain.model.Contact
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object ContactsDB {
    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts: StateFlow<List<Contact>> = _contacts

    private const val picURL = "https://www.petful.com/wp-content/uploads/2016/07/british-shorthair.jpg"

    init {
        initializeDB()
    }

    private fun initializeDB() {
        _contacts.value = (1..10).map { i ->
            Contact(picURL, "Person $i", "Unique career")
        }
    }

    fun getContactWithId(id: Long): Contact? {
        return _contacts.value.find { it.id == id }
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
        if (_contacts.value.any { it.id == contact.id }) return
        _contacts.value = _contacts.value.toMutableList().apply { add(position, contact) }
    }

}