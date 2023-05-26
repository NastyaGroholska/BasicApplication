package com.shpp.ahrokholska.basicapplication.presentation.ui.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shpp.ahrokholska.basicapplication.presentation.ui.myContacts.MyContactsFragment
import com.shpp.ahrokholska.basicapplication.presentation.ui.myProfile.MyProfileFragment

class ProfileAndContactsAdapter(
    fragmentManager: FragmentManager, lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MyProfileFragment()
            else -> MyContactsFragment()
        }
    }
}