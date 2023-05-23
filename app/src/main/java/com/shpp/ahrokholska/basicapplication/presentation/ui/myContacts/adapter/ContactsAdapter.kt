package com.shpp.ahrokholska.basicapplication.presentation.ui.myContacts.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shpp.ahrokholska.basicapplication.databinding.ContactsItemBinding
import com.shpp.ahrokholska.basicapplication.domain.model.Contact
import com.shpp.ahrokholska.basicapplication.presentation.ui.myContacts.adapter.diffCallback.ContactsDiffCallback
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
            with(binding) {
                contactsTextName.text = contact.name
                contactsTextCareer.text = contact.career
                contactsImagePhoto.loadFromURL(contact.picture)
                contactsImageBin.isEnabled = true
                root.isEnabled = true
            }
            setListeners(contact)
        }

        private fun setTransitionName(view: View, name: String): Pair<View, String> {
            view.transitionName = name
            return view to name
        }

        private fun setListeners(contact: Contact) {
            with(binding) {
                contactsImageBin.setOnClickListener {
                    it.isEnabled = false
                    binding.root.isEnabled = false
                    onBinClick(contact, adapterPosition)
                }
                root.setOnClickListener {
                    onItemClick(
                        contact, arrayOf(
                            setTransitionName(
                                contactsImagePhoto, TRANSITION_NAME_IMAGE + contact.id
                            ),
                            setTransitionName(
                                contactsTextName, TRANSITION_NAME_USER_NAME + contact.id
                            ),
                            setTransitionName(
                                contactsTextCareer, TRANSITION_NAME_CAREER + contact.id
                            )
                        )
                    )
                }
            }
        }
    }
}