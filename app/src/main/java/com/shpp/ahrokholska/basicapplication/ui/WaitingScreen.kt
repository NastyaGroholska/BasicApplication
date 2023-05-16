package com.shpp.ahrokholska.basicapplication.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.shpp.ahrokholska.basicapplication.R
import com.shpp.ahrokholska.basicapplication.utils.Constants.STORED_EMAIL_KEY
import com.shpp.ahrokholska.basicapplication.utils.Constants.STORED_USER_NAME_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

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
            val savedEmail = userViewModel.readStringFromStore(STORED_EMAIL_KEY).first()
            if (savedEmail == "") {
                launch(Dispatchers.Main) {
                    navController.navigate(R.id.action_waitingScreen_to_signUp)
                }
            } else {
                val savedUserName = userViewModel.readStringFromStore(STORED_USER_NAME_KEY).first()
                delay(3000) // TODO:  remove in final version
                launch(Dispatchers.Main) {
                    navController.navigate(
                        WaitingScreenDirections.actionWaitingScreenToMyProfile(savedUserName)
                    )
                }
            }
        }
    }
}