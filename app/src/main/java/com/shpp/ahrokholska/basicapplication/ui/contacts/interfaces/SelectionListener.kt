package com.shpp.ahrokholska.basicapplication.ui.contacts.interfaces

interface SelectionListener {
    fun clearSelection()
    fun addItemToSelection(itemPos: Int)
    fun removeItemFromSelection(itemPos: Int)
}