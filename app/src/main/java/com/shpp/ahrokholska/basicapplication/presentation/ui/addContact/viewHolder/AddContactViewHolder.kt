package com.shpp.ahrokholska.basicapplication.presentation.ui.addContact.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.shpp.ahrokholska.basicapplication.databinding.AddContactsItemBinding
import com.shpp.ahrokholska.basicapplication.domain.model.Contact
import com.shpp.ahrokholska.basicapplication.presentation.ui.addContact.State
import com.shpp.ahrokholska.basicapplication.presentation.utils.Constants
import com.shpp.ahrokholska.basicapplication.presentation.utils.ext.loadFromURL

class AddContactViewHolder(
    private val binding: AddContactsItemBinding,
    private val onAdd: (Int, Long) -> Unit
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
                onAdd(adapterPosition, contact.id)
            }
        }
    }

    private fun setState(state: State) {
        with(binding) {
            when (state) {
                State.Normal -> {
                    contactsImagePlus.visibility = View.VISIBLE
                    contactsProgressBar.visibility = View.GONE
                    contactsImageFinish.visibility = View.GONE
                    contactsTextError.visibility = View.GONE
                }

                State.Loading -> {
                    contactsImagePlus.visibility = View.GONE
                    contactsProgressBar.visibility = View.VISIBLE
                    contactsImageFinish.visibility = View.GONE
                    contactsTextError.visibility = View.GONE
                }

                State.Loaded -> {
                    contactsImagePlus.visibility = View.GONE
                    contactsProgressBar.visibility = View.GONE
                    contactsImageFinish.visibility = View.VISIBLE
                    contactsTextError.visibility = View.GONE
                }

                State.Failed -> {
                    contactsImagePlus.visibility = View.VISIBLE
                    contactsProgressBar.visibility = View.GONE
                    contactsImageFinish.visibility = View.GONE
                    contactsTextError.visibility = View.VISIBLE
                }
            }
        }
    }
}