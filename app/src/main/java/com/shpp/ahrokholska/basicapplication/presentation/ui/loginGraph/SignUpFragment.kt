package com.shpp.ahrokholska.basicapplication.presentation.ui.loginGraph

import com.google.android.material.snackbar.Snackbar
import com.shpp.ahrokholska.basicapplication.R
import com.shpp.ahrokholska.basicapplication.databinding.FragmentSignUpBinding
import com.shpp.ahrokholska.basicapplication.presentation.ui.BaseFragment
import com.shpp.ahrokholska.basicapplication.presentation.utils.InputHandler
import com.shpp.ahrokholska.basicapplication.presentation.utils.Validator

class SignUpFragment : BaseFragment<FragmentSignUpBinding>(FragmentSignUpBinding::inflate) {

    override fun setListeners() {
        with(binding) {
            val email = InputHandler(
                tietEmail, tilEmail,
                getString(R.string.incorrect_mail), Validator::isEmailValid
            )
            val password = InputHandler(
                tietPassword, tilPassword,
                getString(R.string.incorrect_password),
                Validator::isPasswordValid
            )
            textSignIn.setOnClickListener {
                navController.navigateUp()
            }
            setRegisterButtonListener(email, password)
        }
    }

    private fun setRegisterButtonListener(email: InputHandler, password: InputHandler) {
        binding.btnRegister.setOnClickListener {
            val isEmailValid = email.processInput()
            val isPasswordValid = password.processInput()

            if (isEmailValid && isPasswordValid) {
                navController.navigate(
                    SignUpFragmentDirections.actionSignUpToSignUpExtended(
                        email.getInputText(), password.getInputText(),
                        binding.checkRememberMe.isChecked
                    )
                )
            } else {
                Snackbar.make(it, R.string.signup_error, Snackbar.LENGTH_SHORT)
                    .setAnchorView(it).show()
            }
        }
    }
}