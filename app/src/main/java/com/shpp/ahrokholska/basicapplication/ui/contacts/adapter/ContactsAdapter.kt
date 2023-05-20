package com.shpp.ahrokholska.basicapplication.ui.contacts.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.shpp.ahrokholska.basicapplication.data.Contact
import com.shpp.ahrokholska.basicapplication.databinding.ContactsItemBinding
import com.shpp.ahrokholska.basicapplication.databinding.ContactsItemMultiselectBinding
import com.shpp.ahrokholska.basicapplication.ui.contacts.ContactsNormalItemListener
import com.shpp.ahrokholska.basicapplication.ui.contacts.viewholders.ContactsMultiselectViewHolder
import com.shpp.ahrokholska.basicapplication.ui.contacts.viewholders.ContactsNormalViewHolder
import com.shpp.ahrokholska.basicapplication.ui.contacts.viewholders.ContactsViewHolder

class ContactsAdapter(
    private val listener: ContactsNormalItemListener,
    private val bigBinBtn: View, private val onBigBinClick: (List<Contact>, List<Int>) -> Unit
) :
    ListAdapter<Contact, ContactsViewHolder>(ContactsDiffCallback()) {
    private var multiselectEnabled = false
    private val selectedPosition = mutableListOf<Int>()

    private enum class ViewType(val code: Int) {
        Normal(0), Multiselect(1)
    }

    init {
        bigBinBtn.setOnClickListener {
            disableMultiselect()
            selectedPosition.sort()
            onBigBinClick(selectedPosition.map { getItem(it) }, selectedPosition.map { it })
            selectedPosition.clear()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        if (multiselectEnabled) {
            return ContactsMultiselectViewHolder(
                ContactsItemMultiselectBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), ::onMultiselectItemStateChange
            )
        }
        return ContactsNormalViewHolder(
            ContactsItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), ::enableMultiselect
        )
    }

    override fun getItemViewType(position: Int): Int {
        return if (multiselectEnabled) ViewType.Multiselect.code else ViewType.Normal.code
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        when (holder) {
            is ContactsNormalViewHolder -> holder.bindTo(getItem(position), listener)
            is ContactsMultiselectViewHolder -> holder.bindTo(
                getItem(position), selectedPosition.contains(position)
            )
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun enableMultiselect(adapterPosition: Int) {
        multiselectEnabled = true
        bigBinBtn.visibility = View.VISIBLE
        selectedPosition.add(adapterPosition)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun disableMultiselect() {
        multiselectEnabled = false
        bigBinBtn.visibility = View.GONE
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onMultiselectItemStateChange(isChecked: Boolean, adapterPosition: Int) {
        if (isChecked) {
            selectedPosition.add(adapterPosition)
        } else {
            selectedPosition.remove(adapterPosition)
        }
        if (selectedPosition.isEmpty()) {
            disableMultiselect()
        }
    }
}