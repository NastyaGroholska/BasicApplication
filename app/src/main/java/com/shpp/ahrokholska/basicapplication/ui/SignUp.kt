package com.shpp.ahrokholska.basicapplication.ui

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.shpp.ahrokholska.basicapplication.*
import com.shpp.ahrokholska.basicapplication.databinding.ActivitySignUpBinding
import com.shpp.ahrokholska.basicapplication.utils.Constants.STORED_EMAIL_KEY
import com.shpp.ahrokholska.basicapplication.utils.Constants.USER_NAME
import com.shpp.ahrokholska.basicapplication.utils.Parser
import com.shpp.ahrokholska.basicapplication.utils.ext.readStringFromStore
import com.shpp.ahrokholska.basicapplication.utils.ext.writeStringToStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SignUp : AppCompatActivity() {
    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkForAutoLogin()

        setContentView(binding.root)

        setListeners()
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
                if (binding.checkRememberMe.isChecked) {
                    lifecycleScope.launch {
                        writeStringToStore(STORED_EMAIL_KEY, emailText)
                    }
                }
                openMyProfile(Parser.getUserName(emailText))
            } else {
                Snackbar.make(it, R.string.signup_error, Snackbar.LENGTH_SHORT)
                    .setAnchorView(it).show()
            }
        }
    }

    /**
     * If an email is stored in DataStore, opens MyProfile activity
     */
    private fun checkForAutoLogin() {
        lifecycleScope.launch(Dispatchers.IO) {
            val savedEmail = readStringFromStore(STORED_EMAIL_KEY).first()
            if (savedEmail != "") {
                openMyProfile(Parser.getUserName(savedEmail))
            }
        }
    }

    private fun openMyProfile(userName: String) {
        startActivity(Intent(this, MyProfile::class.java).apply {
            putExtra(USER_NAME, userName)
        }, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
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