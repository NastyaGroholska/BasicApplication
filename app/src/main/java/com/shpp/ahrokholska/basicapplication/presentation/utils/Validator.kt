package com.shpp.ahrokholska.basicapplication.presentation.utils

import android.util.Patterns

object Validator {
    private const val MIN_PASSWORD_LENGTH = 8

    fun isEmailValid(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPasswordValid(password: String): Boolean {
        return password.isNotEmpty() && password.length >= MIN_PASSWORD_LENGTH
    }
}