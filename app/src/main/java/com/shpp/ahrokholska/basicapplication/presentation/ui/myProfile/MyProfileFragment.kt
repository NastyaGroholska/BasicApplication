package com.shpp.ahrokholska.basicapplication.presentation.ui.myProfile

import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.shpp.ahrokholska.basicapplication.databinding.FragmentMyProfileBinding
import com.shpp.ahrokholska.basicapplication.presentation.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyProfileFragment :
    BaseFragment<FragmentMyProfileBinding>(FragmentMyProfileBinding::inflate) {
    private val viewModel: MyProfileViewModel by viewModels()

    override fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userName.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                it?.let {
                    with(binding) {
                        textName.text = it.name
                        textCareer.text = it.career
                        textAddress.text = it.address
                    }
                }
            }
        }
    }

    override fun setListeners() {
        binding.btnViewMyContacts.setOnClickListener {
            navController.navigate(MyProfileFragmentDirections.actionMyProfileToMyContacts())
        }
        binding.btnEditProfile.setOnClickListener {
            navController.navigate(MyProfileFragmentDirections.actionMyProfileToEditProfile())
        }
    }
}