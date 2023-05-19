package com.shpp.ahrokholska.basicapplication.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.shpp.ahrokholska.basicapplication.R
import com.shpp.ahrokholska.basicapplication.ui.viewmodels.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WaitingScreen : Fragment(R.layout.fragment_waiting_screen) {
    private val navController by lazy { findNavController() }
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkForAutoLogin()
    }

    /**
     * If an email is stored in DataStore, opens MyProfile fragment
     */
    private fun checkForAutoLogin() {
        lifecycleScope.launch(Dispatchers.IO) {
            delay(3000) // TODO:  remove in final version
            userViewModel.userNameStateFlow.collect {
                withContext(Dispatchers.Main){
                    if (it != "") {
                        navController.navigate(R.id.action_waitingScreen_to_myProfile)
                    } else {
                        navController.navigate(R.id.action_waitingScreen_to_signUp)
                    }
                }
            }
        }
    }
}