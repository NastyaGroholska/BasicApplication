package com.shpp.ahrokholska.basicapplication.presentation.ui.waitingScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.shpp.ahrokholska.basicapplication.R
import com.shpp.ahrokholska.basicapplication.databinding.FragmentWaitingScreenBinding
import com.shpp.ahrokholska.basicapplication.presentation.ui.NavigationBaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WaitingScreenFragment : NavigationBaseFragment<FragmentWaitingScreenBinding>() {
    private val viewModel: WaitingScreenViewModel by viewModels()

    override fun inflate(inflater: LayoutInflater, container: ViewGroup?):
            FragmentWaitingScreenBinding {
        return FragmentWaitingScreenBinding.inflate(inflater, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkForAutoLogin()
    }

    private fun checkForAutoLogin() {
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.isAutoLoginEnabled.collect { isAutoLoginEnabled ->
                withContext(Dispatchers.Main) {
                    if (isAutoLoginEnabled) {
                        navController.navigate(R.id.action_waitingScreen_to_myProfile)
                    } else {
                        navController.navigate(R.id.action_waitingScreen_to_signUp)
                    }
                }
            }
        }
    }
}