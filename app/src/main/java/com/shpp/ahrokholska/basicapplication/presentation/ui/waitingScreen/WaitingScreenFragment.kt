package com.shpp.ahrokholska.basicapplication.presentation.ui.waitingScreen

import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.shpp.ahrokholska.basicapplication.databinding.FragmentWaitingScreenBinding
import com.shpp.ahrokholska.basicapplication.presentation.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WaitingScreenFragment :
    BaseFragment<FragmentWaitingScreenBinding>(FragmentWaitingScreenBinding::inflate) {
    private val viewModel: WaitingScreenViewModel by viewModels()

    override fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isAutoLoginEnabled.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                it?.let {
                    if (it) {
                        navController.navigate(WaitingScreenFragmentDirections.actionWaitingScreenToMyProfile())
                    } else {
                        navController.navigate(WaitingScreenFragmentDirections.actionWaitingScreenToSignUp())
                    }
                }
            }
        }
    }
}