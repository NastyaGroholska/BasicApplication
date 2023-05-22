package com.shpp.ahrokholska.basicapplication.ui.contacts.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.shpp.ahrokholska.basicapplication.data.Contact
import com.shpp.ahrokholska.basicapplication.databinding.ContactsItemBinding
import com.shpp.ahrokholska.basicapplication.ui.contacts.interfaces.ContactsNormalItemListener
import com.shpp.ahrokholska.basicapplication.utils.Constants
import com.shpp.ahrokholska.basicapplication.utils.ext.loadFromURL

class ContactsNormalViewHolder(
    private val binding: ContactsItemBinding,
    private val enableMultiselect: (Int) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindTo(contact: Contact, listener: ContactsNormalItemListener) {
        with(binding) {
            contactsTextName.text = contact.name
            contactsTextCareer.text = contact.career
            contactsImagePhoto.loadFromURL(contact.picture)
            contactsImageBin.isEnabled = true
            root.isEnabled = true

            val transitionPairs = arrayOf(
                setTransitionName(
                    contactsImagePhoto, Constants.TRANSITION_NAME_IMAGE + contact.id
                ), setTransitionName(
                    contactsTextName, Constants.TRANSITION_NAME_USER_NAME + contact.id
                ), setTransitionName(
                    contactsTextCareer, Constants.TRANSITION_NAME_CAREER + contact.id
                )
            )
            setListeners(contact, transitionPairs, listener)
        }
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