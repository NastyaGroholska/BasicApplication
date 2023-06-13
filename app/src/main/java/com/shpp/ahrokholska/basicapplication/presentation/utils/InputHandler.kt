package com.shpp.ahrokholska.basicapplication.presentation.utils

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.shpp.ahrokholska.basicapplication.R


/**
 * Checks input for errors
 */
class InputHandler(
    private val input: TextInputEditText, private val errorDisplay: TextInputLayout,
    private val errorMessage: String, private val isValid: (str: String) -> Boolean
) {
    init {
        errorDisplay.setErrorTextAppearance(R.style.errorMessage)
        setInputErrorListeners()
    }

    /**
     * Adds listener to [input] when [input] looses focus or
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