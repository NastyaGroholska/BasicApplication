package com.shpp.ahrokholska.basicapplication.presentation.ui.waitingScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.shpp.ahrokholska.basicapplication.R
import com.shpp.ahrokholska.basicapplication.databinding.FragmentWaitingScreenBinding
import com.shpp.ahrokholska.basicapplication.presentation.ui.BaseFragment
import kotlinx.coroutines.launch

class WaitingScreenFragment : BaseFragment<FragmentWaitingScreenBinding>() {
    private val viewModel: WaitingScreenViewModel by viewModels()

    override fun inflate(inflater: LayoutInflater, container: ViewGroup?):
            FragmentWaitingScreenBinding {
        return FragmentWaitingScreenBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkForAutoLogin()
    }

    private fun checkForAutoLogin() {
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