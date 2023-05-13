package com.shpp.ahrokholska.basicapplication.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.max

class ContactsDB {
    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts: StateFlow<List<Contact>> = _contacts

    private val picURL = "https://www.petful.com/wp-content/uploads/2016/07/british-shorthair.jpg"

    fun initializeDB() {
        _contacts.value = (1..10).map { i ->
            Contact(i, picURL, "Person $i", "Unique career")
        }
    }

    private fun findNewId(): Int {
        // but not safely. What will be if delete few elements in the end of list and after that add new one?
        // https://dou.ua/lenta/articles/interview-questions-android-developer/ item #115
        // id:Long = UUID.randomUUID().mostSignificantBits
        var max = 0
        contacts.value.forEach {
            max = max(max, it.id)
        }
        return max + 1
    }

    fun addContact(name: String, career: String) {
        _contacts.update { value ->
            val mutableValue = value.toMutableList()
            mutableValue.add(Contact(findNewId(), picURL, name, career))
            mutableValue
        }
    }

    fun removeWithId(id: Int) {
        // TODO alternative: _contacts.value = _contacts.value.toMutableList().apply { removeIf{it.id == id} }
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