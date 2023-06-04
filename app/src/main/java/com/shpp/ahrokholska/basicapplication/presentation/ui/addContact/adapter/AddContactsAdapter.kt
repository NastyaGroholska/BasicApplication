package com.shpp.ahrokholska.basicapplication.presentation.ui.addContact.adapter

import android.annotation.SuppressLint
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
    private var states: Array<Pair<Long, State>> = emptyArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddContactViewHolder {
        return AddContactViewHolder(
            AddContactsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onAdd
        )
    }

    override fun submitList(list: List<Contact>?) {
        super.submitList(list)
        states = if (list == null) {
            emptyArray()
        } else {
            Array(list.size) { list[it].id to State.Normal }
        }
    }

    override fun onBindViewHolder(holder: AddContactViewHolder, position: Int) {
        holder.bindTo(
            getItem(position),
            states.find { it.first == currentList[position].id }?.second ?: State.Normal
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setStates(states: Array<Pair<Long, State>>) {
        if (this.states.size != states.size) {
            this.states = states
            notifyDataSetChanged()
            return
        }
        states.forEachIndexed { index, state ->
            if (this.states[index] != states[index]) {
                this.states[index] = state
                notifyItemChanged(index)
            }
        }
    }
}