package com.shpp.ahrokholska.basicapplication.presentation.ui.waitingScreen

import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.shpp.ahrokholska.basicapplication.R
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
            viewModel.isAutoLoginEnabled.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { isAutoLoginEnabled ->
                    if (isAutoLoginEnabled) {
                        navController.navigate(R.id.action_waitingScreen_to_myProfile)
                    } else {
                        navController.navigate(R.id.action_waitingScreen_to_signUp)
                    }
                }
        }
    }
}