package com.shpp.ahrokholska.basicapplication.presentation.ui.myProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.shpp.ahrokholska.basicapplication.R
import com.shpp.ahrokholska.basicapplication.databinding.FragmentMyProfileBinding
import kotlinx.coroutines.launch

class MyProfileFragment : Fragment() {
    private var _binding: FragmentMyProfileBinding? = null
    private val binding get() = _binding!!
    private val navController by lazy { findNavController() }
    private val viewModel: MyProfileViewModel by viewModels()

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
            viewModel.userName.collect {
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