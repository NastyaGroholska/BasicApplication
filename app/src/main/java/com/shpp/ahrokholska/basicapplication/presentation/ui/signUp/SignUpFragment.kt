package com.shpp.ahrokholska.basicapplication.presentation.ui.signUp

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.shpp.ahrokholska.basicapplication.*
import com.shpp.ahrokholska.basicapplication.databinding.FragmentSignUpBinding
import com.shpp.ahrokholska.basicapplication.presentation.ui.BaseFragment
import com.shpp.ahrokholska.basicapplication.presentation.utils.InputHandler
import com.shpp.ahrokholska.basicapplication.presentation.utils.Parser
import com.shpp.ahrokholska.basicapplication.presentation.utils.Validator

class SignUpFragment : BaseFragment<FragmentSignUpBinding>(FragmentSignUpBinding::inflate) {
    private val viewModel: SignUpViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        with(binding) {
            val email = InputHandler(
                tietEmail, tilEmail,
                getString(R.string.incorrect_mail), Validator::isEmailValid
            )
            val password = InputHandler(
                tietPassword,
                tilPassword,
                getString(R.string.incorrect_password),
                Validator::isPasswordValid
            )
            setRegisterButtonListener(email, password)
        }
    }

    private fun setRegisterButtonListener(email: InputHandler, password: InputHandler) {
        binding.btnRegister.setOnClickListener {
            val isEmailValid = email.processInput()
            val isPasswordValid = password.processInput()

            if (isEmailValid && isPasswordValid) {
                val emailText = email.getInputText()
                val parsedUserName = Parser.getUserName(emailText)
                viewModel.saveIsAutoLoginEnabled(binding.checkRememberMe.isChecked)
                viewModel.saveUsername(parsedUserName)
                navController.navigate(R.id.action_signUp_to_myProfile)
            } else {
                Snackbar.make(it, R.string.signup_error, Snackbar.LENGTH_SHORT)
                    .setAnchorView(it).show()
            }
        }
    }
}