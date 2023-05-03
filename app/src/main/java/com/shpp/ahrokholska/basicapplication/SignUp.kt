package com.shpp.ahrokholska.basicapplication

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.shpp.ahrokholska.basicapplication.databinding.SignUpBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SignUp : AppCompatActivity() {
    private val binding: SignUpBinding by lazy { SignUpBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkForAutoLogin()

        setContentView(binding.root)

        setListeners()
    }

    private fun setListeners() {
        val email = InputHandler(
            binding.tiedEmail, binding.tilEmail,
            resources.getString(R.string.incorrect_mail), ::isEmailValid
        )
        val password = InputHandler(
            binding.tietPassword, binding.tilPassword,
            resources.getString(R.string.incorrect_password), ::isPasswordValid
        )
        setRegisterButtonListener(email, password)
    }

    private fun setRegisterButtonListener(email: InputHandler, password: InputHandler) {
        binding.btnRegister.setOnClickListener {
            val isEmailValid = email.processInput()
            val isPasswordValid = password.processInput()

            if (isEmailValid && isPasswordValid) {
                val emailText = email.getInputText()
                if (binding.checkRememberMe.isChecked) {
                    lifecycleScope.launch {
                        application.writeStringToStore(STORED_EMAIL_KEY, emailText)
                    }
                }
                openMyProfile(getUserName(emailText))
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
            val savedEmail = application.readStringFromStore(STORED_EMAIL_KEY).first()
            if (savedEmail != "") {
                openMyProfile(getUserName(savedEmail))
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
        private val input: TextInputEditText, private val errorDisplay: TextInputLayout,
        private val errorMessage: String, private val isValid: (str: String) -> Boolean
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