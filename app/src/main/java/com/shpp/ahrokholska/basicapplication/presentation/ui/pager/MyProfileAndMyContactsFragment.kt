package com.shpp.ahrokholska.basicapplication.presentation.ui.pager

import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import com.shpp.ahrokholska.basicapplication.databinding.FragmentMyProfileAndMyContactsBinding
import com.shpp.ahrokholska.basicapplication.presentation.ui.BaseFragment

class MyProfileAndMyContactsFragment :
    BaseFragment<FragmentMyProfileAndMyContactsBinding>(FragmentMyProfileAndMyContactsBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pager.adapter =
            ProfileAndContactsAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = "OBJECT ${(position + 1)}"
        }.attach()
    }

    override fun onDestroyView() {
        binding.pager.adapter = null
        super.onDestroyView()
    }

    fun openTab(ind: Int) {
        binding.pager.currentItem = ind
    }
}