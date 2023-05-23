package com.shpp.ahrokholska.basicapplication.presentation.ui.waitingScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.shpp.ahrokholska.basicapplication.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WaitingScreenFragment : Fragment(R.layout.fragment_waiting_screen) {
    private val navController by lazy { findNavController() }
    private val viewModel: WaitingScreenViewModel by viewModels()

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