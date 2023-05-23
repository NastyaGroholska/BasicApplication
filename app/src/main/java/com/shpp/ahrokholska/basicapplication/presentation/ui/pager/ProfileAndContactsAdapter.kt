package com.shpp.ahrokholska.basicapplication.presentation.ui.pager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shpp.ahrokholska.basicapplication.presentation.ui.myContacts.MyContactsFragment
import com.shpp.ahrokholska.basicapplication.presentation.ui.myProfile.MyProfileFragment

class ProfileAndContactsAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MyProfileFragment()
            else -> MyContactsFragment()
        }
    }
}