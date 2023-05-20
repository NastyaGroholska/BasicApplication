package com.shpp.ahrokholska.basicapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.shpp.ahrokholska.basicapplication.databinding.FragmentMyProfileBinding
import com.shpp.ahrokholska.basicapplication.ui.profile_and_contacts.MyProfileAndMyContactsFragment
import com.shpp.ahrokholska.basicapplication.ui.viewmodels.UserViewModel
import kotlinx.coroutines.launch

class MyProfile : Fragment() {
    private var _binding: FragmentMyProfileBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        setListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setObservers() {
        lifecycleScope.launch {
            userViewModel.userNameStateFlow.collect {
                binding.textName.text = it
            }
        }
    }

    private fun setListeners() {
        binding.btnViewMyContacts.setOnClickListener {
            (parentFragment as MyProfileAndMyContactsFragment).binding.pager.currentItem = 1
        }
    }

}