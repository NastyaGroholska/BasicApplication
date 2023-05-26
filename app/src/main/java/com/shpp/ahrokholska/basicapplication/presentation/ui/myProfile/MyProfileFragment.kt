package com.shpp.ahrokholska.basicapplication.presentation.ui.myProfile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.shpp.ahrokholska.basicapplication.R
import com.shpp.ahrokholska.basicapplication.databinding.FragmentMyProfileBinding
import com.shpp.ahrokholska.basicapplication.presentation.ui.BaseFragment
import kotlinx.coroutines.launch

class MyProfileFragment :
    BaseFragment<FragmentMyProfileBinding>(FragmentMyProfileBinding::inflate) {
    private val viewModel: MyProfileViewModel by viewModels()

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
            navController.navigate(R.id.action_myProfile_to_myContacts)
        }
    }
}