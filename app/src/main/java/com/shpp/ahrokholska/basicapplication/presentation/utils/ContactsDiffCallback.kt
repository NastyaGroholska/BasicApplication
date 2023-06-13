package com.shpp.ahrokholska.basicapplication.presentation.utils

import androidx.recyclerview.widget.DiffUtil
import com.shpp.ahrokholska.basicapplication.domain.model.Contact

class ContactsDiffCallback : DiffUtil.ItemCallback<Contact>() {
    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean =
        oldItem == newItem
}