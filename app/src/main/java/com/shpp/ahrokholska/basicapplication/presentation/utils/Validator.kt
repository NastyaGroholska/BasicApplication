package com.shpp.ahrokholska.basicapplication.presentation.utils

import android.util.Patterns

object Validator {
    private const val MIN_PASSWORD_LENGTH = 8
    private const val MIN_USER_NAME_LENGTH = 3
    private const val MIN_PHONE_NUMBER_LENGTH = 10
    const val DATE_REGEX = "^\\d{4}-\\d{2}-\\d{2}$"

    fun isEmailValid(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPasswordValid(password: String): Boolean {
        return password.isNotEmpty() && password.length >= MIN_PASSWORD_LENGTH
    }

    fun isUserNameValid(useName: String): Boolean {
        return useName.isNotEmpty() && useName.length >= MIN_USER_NAME_LENGTH
    }

    fun isPhoneNumberValid(phoneNumber: String): Boolean {
        return phoneNumber.isNotEmpty() && phoneNumber.count {
            it.isDigit()
        } >= MIN_PHONE_NUMBER_LENGTH
    }

    fun isDateValid(date: String): Boolean {
        return date.isEmpty() || date.matches(Regex(DATE_REGEX))
    }
}