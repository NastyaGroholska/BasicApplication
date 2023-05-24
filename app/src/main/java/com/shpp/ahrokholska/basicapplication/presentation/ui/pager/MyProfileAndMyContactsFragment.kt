package com.shpp.ahrokholska.basicapplication.presentation.ui.pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.shpp.ahrokholska.basicapplication.databinding.FragmentMyProfileAndMyContactsBinding
import com.shpp.ahrokholska.basicapplication.presentation.ui.BaseFragment

class MyProfileAndMyContactsFragment : BaseFragment<FragmentMyProfileAndMyContactsBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pager.adapter = ProfileAndContactsAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = "OBJECT ${(position + 1)}"
        }.attach()
    }

    override fun inflate(inflater: LayoutInflater, container: ViewGroup?):
            FragmentMyProfileAndMyContactsBinding {
        return FragmentMyProfileAndMyContactsBinding.inflate(inflater, container, false)
    }

    fun openTab(ind:Int){
        binding.pager.currentItem = ind
    }
}