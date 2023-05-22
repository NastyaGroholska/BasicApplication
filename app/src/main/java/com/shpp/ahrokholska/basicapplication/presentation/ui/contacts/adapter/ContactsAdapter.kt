package com.shpp.ahrokholska.basicapplication.presentation.ui.contacts.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shpp.ahrokholska.basicapplication.domain.model.Contact
import com.shpp.ahrokholska.basicapplication.databinding.ContactsItemBinding
import com.shpp.ahrokholska.basicapplication.presentation.ui.contacts.adapter.diffCallback.ContactsDiffCallback
import com.shpp.ahrokholska.basicapplication.presentation.utils.Constants.TRANSITION_NAME_CAREER
import com.shpp.ahrokholska.basicapplication.presentation.utils.Constants.TRANSITION_NAME_IMAGE
import com.shpp.ahrokholska.basicapplication.presentation.utils.Constants.TRANSITION_NAME_USER_NAME
import com.shpp.ahrokholska.basicapplication.presentation.utils.ext.loadFromURL

class ContactsAdapter(
    private val onBinClick: (Contact, Int) -> Unit,
    private val onItemClick: (Contact, Array<Pair<View, String>>) -> Unit
) :
    ListAdapter<Contact, ContactsAdapter.ContactsViewHolder>(ContactsDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        return ContactsViewHolder(
            ContactsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    inner class ContactsViewHolder(private val binding: ContactsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindTo(contact: Contact) {
            // FIXME HARDCODED NUMBER
            val transitionPairs = Array<Pair<View, String>>(3) { binding.root to "" }
            with(binding) {
                contactsTextName.text = contact.name
                contactsTextCareer.text = contact.career
                contactsImagePhoto.loadFromURL(contact.picture)
                contactsImageBin.isEnabled = true
                root.isEnabled = true

                transitionPairs[0] =
                    setTransitionName(contactsImagePhoto, TRANSITION_NAME_IMAGE + contact.id)
                transitionPairs[1] =
                    setTransitionName(contactsTextName, TRANSITION_NAME_USER_NAME + contact.id)
                transitionPairs[2] =
                    setTransitionName(contactsTextCareer, TRANSITION_NAME_CAREER + contact.id)
            }
            setListeners(contact, transitionPairs)
        }

        private fun setTransitionName(view: View, name: String): Pair<View, String> {
            view.transitionName = name
            return view to name
        }

        private fun setListeners(contact: Contact, transitionPairs: Array<Pair<View, String>>) {
            binding.contactsImageBin.setOnClickListener {
                it.isEnabled = false
                binding.root.isEnabled = false
                onBinClick(contact, adapterPosition)
            }
            binding.root.setOnClickListener {
                onItemClick(contact, transitionPairs)
            }
        }
    }
}