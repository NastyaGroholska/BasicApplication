package com.shpp.ahrokholska.basicapplication.presentation.ui.addContact.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.shpp.ahrokholska.basicapplication.databinding.AddContactsItemBinding
import com.shpp.ahrokholska.basicapplication.domain.model.Contact
import com.shpp.ahrokholska.basicapplication.presentation.ui.addContact.State
import com.shpp.ahrokholska.basicapplication.presentation.utils.Constants
import com.shpp.ahrokholska.basicapplication.presentation.utils.ext.gone
import com.shpp.ahrokholska.basicapplication.presentation.utils.ext.loadFromURL
import com.shpp.ahrokholska.basicapplication.presentation.utils.ext.visible

class AddContactViewHolder(
    private val binding: AddContactsItemBinding,
    private val onAdd: (Long) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindTo(contact: Contact, state: State) {
        with(binding) {
            contactsTextName.text = contact.name
            contactsTextCareer.text = contact.career
            contactsImagePhoto.loadFromURL(Constants.PICTURE_URL)
            setState(state)
        }
        setListeners(contact)
    }

    private fun setListeners(contact: Contact) {
        with(binding) {
            contactsImagePlus.setOnClickListener {
                setState(State.Loading)
                onAdd(contact.id)
            }
        }
    }

    private fun setState(state: State) {
        with(binding) {
            when (state) {
                State.Normal -> {
                    contactsImagePlus.visible()
                    contactsProgressBar.gone()
                    contactsImageFinish.gone()
                    contactsTextError.gone()
                }

                State.Loading -> {
                    contactsImagePlus.gone()
                    contactsProgressBar.visible()
                    contactsImageFinish.gone()
                    contactsTextError.gone()
                }

                State.Loaded -> {
                    contactsImagePlus.gone()
                    contactsProgressBar.gone()
                    contactsImageFinish.visible()
                    contactsTextError.gone()
                }

                State.Failed -> {
                    contactsImagePlus.visible()
                    contactsProgressBar.gone()
                    contactsImageFinish.gone()
                    contactsTextError.visible()
                }
            }
        }
    }
}