package com.shpp.ahrokholska.basicapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.shpp.ahrokholska.basicapplication.*
import com.shpp.ahrokholska.basicapplication.databinding.FragmentSignUpBinding
import com.shpp.ahrokholska.basicapplication.utils.Constants.STORED_EMAIL_KEY
import com.shpp.ahrokholska.basicapplication.utils.Constants.STORED_USER_NAME_KEY
import com.shpp.ahrokholska.basicapplication.utils.Parser

class SignUp : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val navController by lazy { findNavController() }
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setListeners() {
        with(binding) {
            val email = InputHandler(
                tietEmail, tilEmail,
                getString(R.string.incorrect_mail), Parser::isEmailValid
            )
            val password = InputHandler(
                tietPassword, tilPassword,
                getString(R.string.incorrect_password), Parser::isPasswordValid
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
                if (binding.checkRememberMe.isChecked) {
                    userViewModel.writeStringToStore(STORED_EMAIL_KEY, emailText)
                }
                userViewModel.writeStringToStore(STORED_USER_NAME_KEY, parsedUserName)
                navController.navigate(SignUpDirections.actionSignUpToMyProfile(parsedUserName))
            } else {
                Snackbar.make(it, R.string.signup_error, Snackbar.LENGTH_SHORT)
                    .setAnchorView(it).show()
            }
        }
    }

    /**
     * Checks input for errors
     */
    inner class InputHandler(
        private val input: TextInputEditText,
        private val errorDisplay: TextInputLayout,
        private val errorMessage: String,
        private val isValid: (str: String) -> Boolean
    ) {
        init {
            errorDisplay.setErrorTextAppearance(R.style.errorMessage)
            setInputErrorListeners()
        }

        /**
         * Adds listener to [input] when [input] loses focus or
         * when the enter key is pressed calls [processInput]
         */
        private fun setInputErrorListeners() {
            input.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    processInput()
                }
            }

            input.setOnEditorActionListener { _, _, _ ->
                processInput()
                true
            }
        }

        fun getInputText(): String {
            return input.text.toString()
        }

        /**
         * If [input] text is valid according to [isValid] then sets error message in [errorDisplay]
         * to null, otherwise sets it to [errorMessage]. Returns if [input] text is valid
         */
        fun processInput(): Boolean {
            return if (isValid(getInputText())) {
                errorDisplay.error = null
                true
            } else {
                errorDisplay.error = errorMessage
                false
            }
        }
    }

}