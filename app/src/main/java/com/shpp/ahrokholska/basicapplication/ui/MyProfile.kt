package com.shpp.ahrokholska.basicapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.shpp.ahrokholska.basicapplication.R
import com.shpp.ahrokholska.basicapplication.utils.Constants.USER_NAME
import com.shpp.ahrokholska.basicapplication.databinding.FragmentMyProfileBinding
import com.shpp.ahrokholska.basicapplication.utils.Constants.STORED_USER_NAME_KEY
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MyProfile : Fragment() {
    private var _binding: FragmentMyProfileBinding? = null
    private val binding get() = _binding!!
    private val navController by lazy { findNavController() }
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userName = arguments?.getString(USER_NAME)
        if (userName != null) {
            binding.textName.text = userName
        } else {
            lifecycleScope.launch {
                binding.textName.text =
                    userViewModel.readStringFromStore(STORED_USER_NAME_KEY).first()
            }
        }
        setListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setListeners() {
        binding.btnViewMyContacts.setOnClickListener {
            navController.navigate(R.id.action_myProfile_to_myContacts)
        }
    }

}