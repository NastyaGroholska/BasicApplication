package com.shpp.ahrokholska.basicapplication.presentation.ui.myContacts.interfaces

interface SelectionListener {
    fun clearSelection()
    fun addItemToSelection(itemPos: Int)
    fun removeItemFromSelection(itemPos: Int)
}