package com.shpp.ahrokholska.basicapplication.ui.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shpp.ahrokholska.basicapplication.data.Contact
import com.shpp.ahrokholska.basicapplication.databinding.ContactsItemBinding
import com.shpp.ahrokholska.basicapplication.utils.ext.loadFromURL

class ContactsAdapter(private val onBinClick: (Contact, Int) -> Unit) :
    ListAdapter<Contact, ContactsAdapter.ViewHolder>(ContactsDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ContactsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    inner class ViewHolder(private val binding: ContactsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindTo(contact: Contact) {
            with(binding) {
                contactsTextName.text = contact.name
                contactsTextCareer.text = contact.career
                contactsImagePhoto.loadFromURL(contact.picture)
            }
            setListeners(contact)
        }

        private fun setListeners(contact: Contact) {
            binding.contactsImageBin.setOnClickListener {
                onBinClick(contact, adapterPosition)
            }
        }
    }
}