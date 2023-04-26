package com.shpp.ahrokholska.basic

import android.os.Bundle
import android.util.Patterns
import androidx.activity.ComponentActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.shpp.ahrokholska.basic.databinding.SignUpBinding

class SignUp : ComponentActivity() {
    private lateinit var binding: SignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignUpBinding.inflate(layoutInflater)

        binding.emailGroup.setErrorTextAppearance(R.style.errorMessage)
        binding.passwordGroup.setErrorTextAppearance(R.style.errorMessage)

        setContentView(binding.root)

        setInputErrorListeners(binding.editTextTextEmailAddress, ::processEmail)
        setInputErrorListeners(binding.editTextTextPassword, ::processPassword)
    }

    /**
     * Adds listener to [viewToListenTo] when [viewToListenTo] loses focus or
     * when the enter key is pressed invokes [processStr]
     * @param viewToListenTo Should have TextInputLayout as a parent
     */
    private fun setInputErrorListeners(
        viewToListenTo: TextInputEditText,
        processStr: (str: String, errorReceiver: TextInputLayout) -> Unit
    ) {
        val errorReceiver = viewToListenTo.parent.parent as TextInputLayout

        viewToListenTo.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                processStr((view as TextInputEditText).text.toString(), errorReceiver)
            }
        }

        viewToListenTo.setOnEditorActionListener { view, actionId, event ->
            processStr((view as TextInputEditText).text.toString(), errorReceiver)
            true
        }
    }

    /**
     * If [email] is valid then sets error message in [errorReceiver] to null,
     * otherwise sets it to @strings/incorrect_mail
     */
    private fun processEmail(email: String, errorReceiver: TextInputLayout) {
        checkString(
            email, ::isEmailValid, resources.getString(R.string.incorrect_mail),
            errorReceiver
        )
    }

    /**
     * If [password] is valid then sets error message in [errorReceiver] to null,
     * otherwise sets it to @strings/incorrect_password
     */
    private fun processPassword(password: String, errorReceiver: TextInputLayout) {
        checkString(
            password, ::isPasswordValid, resources.getString(R.string.incorrect_password),
            errorReceiver
        )
    }

    /**
     * If [str] is valid according to [isValid] then sets error message in [errorReceiver] to null,
     * otherwise sets it to [error]
     */
    private fun checkString(
        str: String, isValid: (str: String) -> Boolean,
        error: String, errorReceiver: TextInputLayout
    ) {
        errorReceiver.error = if (isValid(str)) null else error
    }

    fun isEmailValid(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPasswordValid(password: String): Boolean {
        return password.isNotEmpty() && password.length >= 8
    }
}