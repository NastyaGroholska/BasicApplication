package com.shpp.ahrokholska.basicapplication

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.shpp.ahrokholska.basicapplication.databinding.SignUpBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private const val EMAIL_DOMAIN_SEPARATOR = "@"
private const val DELIMITER_REGEX = "[+._%\\-]+"

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
     * Returns user name parsed from [email].
     * For example: "lucile.alvarado@gmail.com" -> "Lucile Alvarado"
     * @param email Must be valid
     */
    private fun getUserName(email: String): String {
        return email.split(EMAIL_DOMAIN_SEPARATOR)[0].split(DELIMITER_REGEX.toRegex())
            .joinToString(separator = " ") {
                it[0].uppercase() + it.substring(1).lowercase()
            }
    }

    private fun isEmailValid(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.isNotEmpty() && password.length >= 8
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