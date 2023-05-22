package com.shpp.ahrokholska.basicapplication.ui.contacts.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.shpp.ahrokholska.basicapplication.data.Contact
import com.shpp.ahrokholska.basicapplication.databinding.ContactsItemMultiselectBinding
import com.shpp.ahrokholska.basicapplication.utils.ext.loadFromURL

class ContactsMultiselectViewHolder(
    private val binding: ContactsItemMultiselectBinding,
    private val onMultiselectItemStateChange: (Boolean, Int) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindTo(contact: Contact, isChecked: Boolean) {
        with(binding) {
            contactsTextName.text = contact.name
            contactsTextCareer.text = contact.career
            contactsImagePhoto.loadFromURL(contact.picture)
            checkContactMultiselect.isChecked = isChecked
        }
        setListeners()
    }

    private fun setListeners() {
        with(binding) {
            root.setOnClickListener {
                val newCheckVal = !checkContactMultiselect.isChecked
                checkContactMultiselect.isChecked = newCheckVal
                onMultiselectItemStateChange(newCheckVal, adapterPosition)
            }
        }
    }
}