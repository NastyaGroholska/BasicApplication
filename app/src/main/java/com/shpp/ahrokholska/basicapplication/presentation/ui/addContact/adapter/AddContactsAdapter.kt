package com.shpp.ahrokholska.basicapplication.presentation.ui.addContact.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.shpp.ahrokholska.basicapplication.databinding.AddContactsItemBinding
import com.shpp.ahrokholska.basicapplication.domain.model.Contact
import com.shpp.ahrokholska.basicapplication.presentation.ui.addContact.State
import com.shpp.ahrokholska.basicapplication.presentation.ui.addContact.viewHolder.AddContactViewHolder
import com.shpp.ahrokholska.basicapplication.presentation.utils.ContactsDiffCallback

class AddContactsAdapter(private val onAdd: (Int, Long) -> Unit) :
    ListAdapter<Contact, AddContactViewHolder>(ContactsDiffCallback()) {
    private lateinit var states: Array<State>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddContactViewHolder {
        return AddContactViewHolder(
            AddContactsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onAdd
        )
    }

    override fun submitList(list: List<Contact>?) {
        super.submitList(list)
        states = Array(list?.size ?: 0) { State.Normal }
    }

    override fun onBindViewHolder(holder: AddContactViewHolder, position: Int) {
        holder.bindTo(getItem(position), states[position])
    }

    fun setStateAtPosition(pos: Int, state: State) {
        states[pos] = state
        notifyItemChanged(pos)
    }
}