package com.shpp.ahrokholska.basicapplication.presentation.ui.myProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.shpp.ahrokholska.basicapplication.databinding.FragmentMyProfileBinding
import com.shpp.ahrokholska.basicapplication.presentation.ui.BaseFragment
import com.shpp.ahrokholska.basicapplication.presentation.ui.pager.MyProfileAndMyContactsFragment
import kotlinx.coroutines.launch

class MyProfileFragment : BaseFragment<FragmentMyProfileBinding>() {
    private val viewModel: MyProfileViewModel by viewModels()

    override fun inflate(inflater: LayoutInflater, container: ViewGroup?):
            FragmentMyProfileBinding {
        return FragmentMyProfileBinding.inflate(inflater, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        setListeners()
    }

    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userName.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                binding.textName.text = it
            }
        }
    }

    private fun setListeners() {
        binding.btnViewMyContacts.setOnClickListener {
            (parentFragment as? MyProfileAndMyContactsFragment)?.openTab(1)
        }
    }
}