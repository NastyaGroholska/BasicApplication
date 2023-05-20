package com.shpp.ahrokholska.basicapplication.ui.contacts.viewholders

import android.view.View
import com.shpp.ahrokholska.basicapplication.data.Contact
import com.shpp.ahrokholska.basicapplication.databinding.ContactsItemBinding
import com.shpp.ahrokholska.basicapplication.ui.contacts.ContactsNormalItemListener
import com.shpp.ahrokholska.basicapplication.utils.Constants
import com.shpp.ahrokholska.basicapplication.utils.ext.loadFromURL

class ContactsNormalViewHolder(
    private val binding: ContactsItemBinding,
    private val enableMultiselect: (Int) -> Unit
) :
    ContactsViewHolder(binding.root) {

    fun bindTo(contact: Contact, listener: ContactsNormalItemListener) {
        val transitionPairs = Array<Pair<View, String>>(3) { binding.root to "" }
        with(binding) {
            contactsTextName.text = contact.name
            contactsTextCareer.text = contact.career
            contactsImagePhoto.loadFromURL(contact.picture)
            contactsImageBin.isEnabled = true
            root.isEnabled = true

            transitionPairs[0] = setTransitionName(
                contactsImagePhoto, Constants.TRANSITION_NAME_IMAGE + contact.id
            )
            transitionPairs[1] = setTransitionName(
                contactsTextName,
                Constants.TRANSITION_NAME_USER_NAME + contact.id
            )
            transitionPairs[2] = setTransitionName(
                contactsTextCareer, Constants.TRANSITION_NAME_CAREER + contact.id
            )
        }
        setListeners(contact, transitionPairs, listener)
    }

    private fun setTransitionName(view: View, name: String): Pair<View, String> {
        view.transitionName = name
        return view to name
    }

    private fun setListeners(
        contact: Contact, transitionPairs: Array<Pair<View, String>>,
        listener: ContactsNormalItemListener
    ) {
        binding.contactsImageBin.setOnClickListener {
            it.isEnabled = false
            binding.root.isEnabled = false
            listener.onBinClick(contact, adapterPosition)
        }
        binding.root.setOnClickListener {
            listener.onItemClick(contact, transitionPairs)
        }
        binding.root.setOnLongClickListener {
            enableMultiselect(adapterPosition)
            true
        }
    }
}