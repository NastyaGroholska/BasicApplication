package com.shpp.ahrokholska.basicapplication.ui.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shpp.ahrokholska.basicapplication.data.Contact
import com.shpp.ahrokholska.basicapplication.databinding.ContactsItemBinding

class ContactsAdapter(private val onBinClick:(Contact,Int)->Unit) :
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
                Glide.with(root).load(contact.picture).centerCrop().into(contactsImagePhoto)
            }
            setListeners(contact)
        }

        private fun setListeners(contact: Contact) {
            binding.contactsImageBin.setOnClickListener {
                onBinClick(contact,adapterPosition)
            }
        }
    }
}