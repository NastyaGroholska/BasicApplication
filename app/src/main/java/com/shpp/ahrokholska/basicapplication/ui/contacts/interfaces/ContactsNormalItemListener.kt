package com.shpp.ahrokholska.basicapplication.ui.contacts.interfaces

import android.view.View
import com.shpp.ahrokholska.basicapplication.data.Contact

interface ContactsNormalItemListener {
    fun onBinClick(contact: Contact, position: Int)

    /**
    [transitionPairs] are required for shared elements transition animation
     */
    fun onItemClick(contact: Contact, transitionPairs: Array<Pair<View, String>>)
}