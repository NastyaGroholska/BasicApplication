package com.shpp.ahrokholska.basic

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.activity.ComponentActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.shpp.ahrokholska.basic.databinding.SignUpBinding

class SignUp : ComponentActivity() {
    private lateinit var binding: SignUpBinding

    companion object IntentOptions {
        const val USER_NAME = "com.shpp.ahrokholska.basic.SignUp.UserName"
    }

    inner class Wrapper(
        val input: TextInputEditText, val errorDisplay: TextInputLayout, val errorMessage: String,
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

        binding.registerButton.setOnClickListener {
            val isEmailValid = email.processInput()
            val isPasswordValid = password.processInput()

            if (isEmailValid && isPasswordValid) {
                startActivity(Intent(this, MyProfile::class.java).apply {
                    putExtra(USER_NAME, email.input.text.toString()
                        .split("@")[0].split("[+._%\\-]+".toRegex())
                        .joinToString(separator = " ") { it ->
                            it[0].uppercase() + it.substring(1).lowercase()
                        })
                }, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            } else {
                Snackbar.make(it, R.string.signup_error, Snackbar.LENGTH_SHORT).setAnchorView(it)
                    .show()
            }
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.isNotEmpty() && password.length >= 8
    }
}