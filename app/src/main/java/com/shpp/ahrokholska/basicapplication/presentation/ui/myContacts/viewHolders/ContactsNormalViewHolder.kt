package com.shpp.ahrokholska.basicapplication.presentation.ui.myContacts.viewHolders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.shpp.ahrokholska.basicapplication.databinding.ContactsItemBinding
import com.shpp.ahrokholska.basicapplication.domain.model.Contact
import com.shpp.ahrokholska.basicapplication.presentation.utils.Constants
import com.shpp.ahrokholska.basicapplication.presentation.utils.ext.loadFromURL
import com.shpp.ahrokholska.basicapplication.presentation.ui.myContacts.interfaces.ContactsNormalItemListener

class ContactsNormalViewHolder(
    private val binding: ContactsItemBinding,
    private val enableMultiselect: (Int) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {
    var transitionPairs = emptyArray<Pair<View, String>>()

    fun bindTo(contact: Contact, listener: ContactsNormalItemListener) {
        with(binding) {
            contactsTextName.text = contact.name
            contactsTextCareer.text = contact.career
            contactsImagePhoto.loadFromURL(contact.picture)
            contactsImageBin.isEnabled = true
            root.isEnabled = true
            transitionPairs = arrayOf(
                setTransitionName(
                    contactsImagePhoto, Constants.TRANSITION_NAME_IMAGE + contact.id
                ), setTransitionName(
                    contactsTextName, Constants.TRANSITION_NAME_USER_NAME + contact.id
                ), setTransitionName(
                    contactsTextCareer, Constants.TRANSITION_NAME_CAREER + contact.id
                )
            )
        }
        setListeners(contact, listener)
    }

    private fun setTransitionName(view: View, name: String): Pair<View, String> {
        view.transitionName = name
        return view to name
    }

    private fun setListeners(contact: Contact, listener: ContactsNormalItemListener) {
        with(binding) {
            contactsImageBin.setOnClickListener {
                it.isEnabled = false
                root.isEnabled = false
                listener.onBinClick(contact, adapterPosition)
            }
            root.setOnClickListener {
                listener.onItemClick(contact, transitionPairs)
            }
            root.setOnLongClickListener {
                enableMultiselect(adapterPosition)
                true
            }
        }
    }
}