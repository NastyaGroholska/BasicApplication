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
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private const val SIGN_AT = "@"
private const val REGEX_PATTERN = "[+._%\\-]+"

class SignUp : AppCompatActivity() {
    private lateinit var binding: SignUpBinding

    //TODO move const value to separate constants class
    companion object IntentOptions {
        const val USER_NAME = "com.shpp.ahrokholska.basic.SignUp.UserName"
    }

    inner class Wrapper(
        val input: TextInputEditText,
        val errorDisplay: TextInputLayout,
        val errorMessage: String,
        private val isValid: (str: String) -> Boolean
    ) {
        init {
            errorDisplay.setErrorTextAppearance(R.style.errorMessage)
            setInputErrorListeners(input, ::processInput)
        }

        /**
         * Adds listener to [viewToListenTo] when [viewToListenTo] loses focus or
         * when the enter key is pressed invokes [processStr]
         */
        private fun setInputErrorListeners(
            viewToListenTo: TextInputEditText, processStr: () -> Unit
        ) {
            viewToListenTo.setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus) {
                    processStr()
                }
            }

            viewToListenTo.setOnEditorActionListener { view, actionId, event ->
                processStr()
                true
            }
        }

        /**
         * If [input] text is valid according to [isValid] then sets error message in [errorDisplay]
         * to null, otherwise sets it to [errorMessage]. Returns if [input] text is valid
         */
        fun processInput(): Boolean {
            return if (isValid(input.text.toString())) {
                errorDisplay.error = null
                true
            } else {
                errorDisplay.error = errorMessage
                false
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkForAutoLogin()

        binding = SignUpBinding.inflate(layoutInflater)
        val email = Wrapper(
            binding.editTextTextEmailAddress, binding.emailGroup,
            resources.getString(R.string.incorrect_mail), ::isEmailValid
        )
        val password = Wrapper(
            binding.editTextTextPassword, binding.passwordGroup,
            resources.getString(R.string.incorrect_password), ::isPasswordValid
        )

        setContentView(binding.root)

        setListeners(email, password)


    }

    private fun setListeners(email: Wrapper, password: Wrapper) {
        setRegisterButtonLister(email, password)
    }

    private fun setRegisterButtonLister(
        email: Wrapper,
        password: Wrapper
    ) {
        binding.registerButton.setOnClickListener {
            val isEmailValid = email.processInput()
            val isPasswordValid = password.processInput()

            if (isEmailValid && isPasswordValid) {
                val emailText = email.input.text.toString()
                if (binding.rememberMe.isChecked) {
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

    private fun checkForAutoLogin() {
        val savedEmail =
            runBlocking { application.readStringFromStore(STORED_EMAIL_KEY) } //TODO maybe have to invoke in IO thread
        if (savedEmail != "") {
            openMyProfile(getUserName(savedEmail))
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
        return email.split(SIGN_AT)[0].split(REGEX_PATTERN.toRegex())
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
}