package com.shpp.ahrokholska.basicapplication.presentation.ui.loginGraph

import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.shpp.ahrokholska.basicapplication.R
import com.shpp.ahrokholska.basicapplication.databinding.FragmentSignInBinding
import com.shpp.ahrokholska.basicapplication.domain.model.NetworkResponseCode
import com.shpp.ahrokholska.basicapplication.presentation.ui.BaseFragment
import com.shpp.ahrokholska.basicapplication.presentation.utils.InputHandler
import com.shpp.ahrokholska.basicapplication.presentation.utils.Validator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : BaseFragment<FragmentSignInBinding>(FragmentSignInBinding::inflate) {
    private val viewModel: LoginGraphViewModel by hiltNavGraphViewModels(R.id.loginGraph)

    override fun setListeners() {
        with(binding) {
            textSignUn.setOnClickListener {
                navController.navigate(SignInFragmentDirections.actionSignInToSignUp())
            }
            val email = InputHandler(
                tietEmail, tilEmail,
                getString(R.string.incorrect_mail), Validator::isEmailValid
            )
            val password = InputHandler(
                tietPassword, tilPassword,
                getString(R.string.incorrect_password),
                Validator::isPasswordValid
            )
            btnLogin.setOnClickListener {
                val isEmailValid = email.processInput()
                val isPasswordValid = password.processInput()

                if (isEmailValid && isPasswordValid) {
                    sendAuthRequest(email.getInputText(), password.getInputText())
                } else {
                    Snackbar.make(it, R.string.signup_error, Snackbar.LENGTH_SHORT)
                        .setAnchorView(it).show()
                }
            }
        }
    }

    override fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.networkResponse.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                with(binding) {
                    signInProgressWindow.visibility = View.INVISIBLE
                    when (it.code) {
                        NetworkResponseCode.Success -> navController.navigate(
                            SignInFragmentDirections.actionSignInToMyProfile()
                        )

                        NetworkResponseCode.NetworkError -> {
                            AlertDialog.Builder(root.context.applicationContext)
                                .setMessage(getString(R.string.network_error))
                                .setPositiveButton(getString(R.string.retry)) { _, _ ->
                                    sendAuthRequest(
                                        tietEmail.text.toString(), tietPassword.text.toString()
                                    )
                                }.create().show()
                        }

                        NetworkResponseCode.InputError -> {
                            AlertDialog.Builder(root.context.applicationContext)
                                .setMessage(getString(R.string.sign_in_email_error)).create().show()
                        }
                    }
                }
            }
        }
    }

    private fun sendAuthRequest(email: String, password: String) {
        binding.signInProgressWindow.visibility = View.VISIBLE
        if (binding.checkRememberMe.isChecked) {
            viewModel.authorizeAndSaveUser(email, password)
        } else {
            viewModel.authorizeUser(email, password)
        }
    }
}