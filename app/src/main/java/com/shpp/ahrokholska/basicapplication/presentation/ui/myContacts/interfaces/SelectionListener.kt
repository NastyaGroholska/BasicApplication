package com.shpp.ahrokholska.basicapplication.presentation.ui.myContacts.interfaces

interface SelectionListener {
    fun clearSelection()
    fun addItemToSelection(itemId: Long)
    fun removeItemFromSelection(itemPos: Int)
}