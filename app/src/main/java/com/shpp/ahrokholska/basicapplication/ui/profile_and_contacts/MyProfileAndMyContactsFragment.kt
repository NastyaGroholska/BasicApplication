package com.shpp.ahrokholska.basicapplication.ui.profile_and_contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.shpp.ahrokholska.basicapplication.databinding.FragmentMyProfileAndMyContactsBinding

class MyProfileAndMyContactsFragment : Fragment() {
    private var _binding: FragmentMyProfileAndMyContactsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyProfileAndMyContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pager.adapter = ProfileAndContactsAdapter(this, ::selectPage)
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = "OBJECT ${(position + 1)}"
        }.attach()
    }

    private fun selectPage(pageIndex: Int) {
        binding.tabLayout.setScrollPosition(pageIndex, 0f, true)
        binding.pager.currentItem = pageIndex
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}