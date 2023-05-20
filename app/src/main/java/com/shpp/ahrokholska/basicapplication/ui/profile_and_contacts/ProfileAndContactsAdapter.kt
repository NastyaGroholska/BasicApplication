package com.shpp.ahrokholska.basicapplication.ui.profile_and_contacts

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shpp.ahrokholska.basicapplication.ui.MyProfile
import com.shpp.ahrokholska.basicapplication.ui.contacts.MyContacts

class ProfileAndContactsAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MyProfile()
            else -> MyContacts()
        }
    }
}