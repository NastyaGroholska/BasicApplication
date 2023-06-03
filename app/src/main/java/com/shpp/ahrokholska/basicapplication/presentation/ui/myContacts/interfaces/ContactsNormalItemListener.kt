package com.shpp.ahrokholska.basicapplication.presentation.ui.myContacts.interfaces

import android.view.View
import com.shpp.ahrokholska.basicapplication.domain.model.Contact

interface ContactsNormalItemListener {
    fun onBinClick(contact: Contact)

    /**
    [transitionPairs] are required for shared elements transition animation
     */
    fun onItemClick(contact: Contact, transitionPairs: Array<Pair<View, String>>)
}