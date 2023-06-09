package com.shpp.ahrokholska.basicapplication.presentation.ui.myContacts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shpp.ahrokholska.basicapplication.databinding.ContactsItemBinding
import com.shpp.ahrokholska.basicapplication.databinding.ContactsItemMultiselectBinding
import com.shpp.ahrokholska.basicapplication.domain.model.Contact
import com.shpp.ahrokholska.basicapplication.presentation.ui.myContacts.interfaces.ContactsNormalItemListener
import com.shpp.ahrokholska.basicapplication.presentation.ui.myContacts.interfaces.SelectionListener
import com.shpp.ahrokholska.basicapplication.presentation.ui.myContacts.viewHolders.ContactsMultiselectViewHolder
import com.shpp.ahrokholska.basicapplication.presentation.ui.myContacts.viewHolders.ContactsNormalViewHolder
import com.shpp.ahrokholska.basicapplication.presentation.utils.ContactsDiffCallback

class ContactsAdapter(
    private val itemListener: ContactsNormalItemListener,
    private val selectionListener: SelectionListener,
    private val onMultiselectItemStateChangeCallback: (Boolean) -> Unit
) :
    ListAdapter<Contact, RecyclerView.ViewHolder>(ContactsDiffCallback()) {
    private var isMultiselectEnabled = false
    private var selectedIds = listOf<Long>()

    private enum class ViewType {
        Normal, Multiselect
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (isMultiselectEnabled) {
            return ContactsMultiselectViewHolder(
                ContactsItemMultiselectBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), ::onMultiselectItemStateChange
            )
        }
        return ContactsNormalViewHolder(
            ContactsItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), selectionListener::addItemToSelection
        )
    }

    override fun getItemViewType(position: Int): Int {
        return if (isMultiselectEnabled) ViewType.Multiselect.ordinal else ViewType.Normal.ordinal
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ContactsNormalViewHolder -> holder.bindTo(getItem(position), itemListener)
            is ContactsMultiselectViewHolder -> holder.bindTo(
                getItem(position), selectedIds.contains(getItem(position).id)
            )
        }
    }

    fun deleteMultiselectItems(): List<Contact> {
        val itemsToRemove = selectedIds.map { currentList.first { item -> item.id == it } }
        selectionListener.clearSelection()
        return itemsToRemove
    }

    fun changeMultiselectState(isMultiselectEnabled: Boolean) {
        this.isMultiselectEnabled = isMultiselectEnabled
    }

    fun changeMultiselectItems(selectedPositions: List<Long>) {
        this.selectedIds = selectedPositions
    }

    fun getPositionOfId(id: Long): Int {
        return currentList.indexOfFirst { it.id == id }
    }

    private fun onMultiselectItemStateChange(isChecked: Boolean, adapterPosition: Int) {
        onMultiselectItemStateChangeCallback(isChecked)
        if (isChecked) {
            selectionListener.addItemToSelection(currentList[adapterPosition].id)
        } else {
            selectionListener.removeItemFromSelection(currentList[adapterPosition].id)
        }
    }
}