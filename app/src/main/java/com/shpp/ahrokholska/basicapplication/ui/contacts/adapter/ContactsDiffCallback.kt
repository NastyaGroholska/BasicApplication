package com.shpp.ahrokholska.basicapplication.ui.contacts.adapter

import androidx.recyclerview.widget.DiffUtil
import com.shpp.ahrokholska.basicapplication.data.Contact

class ContactsDiffCallback : DiffUtil.ItemCallback<Contact>() {
    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean =
        oldItem == newItem
}