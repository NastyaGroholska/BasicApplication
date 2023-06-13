package com.shpp.ahrokholska.basicapplication.presentation.utils

object Parser {
    private const val EMAIL_DOMAIN_SEPARATOR = "@"
    private const val DELIMITER_REGEX = "[+._%\\-]+"
    private const val DATE_REGEX = "^\\d{4}-\\d{2}-\\d{2}$"

    /**
     * Returns user name parsed from [email].
     * For example: "lucile.alvarado@gmail.com" -> "Lucile Alvarado"
     */
    fun getUserName(email: String): String {
        return if (!Validator.isEmailValid(email)) "" else
            email.substringBefore(EMAIL_DOMAIN_SEPARATOR).split(DELIMITER_REGEX.toRegex())
                .joinToString(separator = " ") {
                    if (it.isEmpty()) {
                        it
                    } else {
                        it[0].uppercase() + it.substring(1).lowercase()
                    }
                }
    }

    fun getDate(date: String): String {
        return DATE_REGEX.substring(0, DATE_REGEX.length - 1).toRegex()
            .find(date)?.value ?: ""
    }
}