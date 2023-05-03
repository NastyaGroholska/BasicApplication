package com.shpp.ahrokholska.basicapplication

import android.util.Patterns

private const val EMAIL_DOMAIN_SEPARATOR = "@"
private const val DELIMITER_REGEX = "[+._%\\-]+"

/**
 * Returns user name parsed from [email].
 * For example: "lucile.alvarado@gmail.com" -> "Lucile Alvarado"
 * @param email Must be valid
 */
fun getUserName(email: String): String {
    return email.split(EMAIL_DOMAIN_SEPARATOR)[0].split(DELIMITER_REGEX.toRegex())
        .joinToString(separator = " ") {
            it[0].uppercase() + it.substring(1).lowercase()
        }
}

 fun isEmailValid(email: String): Boolean {
    return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

 fun isPasswordValid(password: String): Boolean {
    return password.isNotEmpty() && password.length >= 8
}