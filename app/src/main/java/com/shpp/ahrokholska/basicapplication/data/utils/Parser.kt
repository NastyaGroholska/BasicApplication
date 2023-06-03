package com.shpp.ahrokholska.basicapplication.data.utils

import com.google.gson.Gson

object Parser {
    inline fun <reified T> parseBody(body: String): T {
        var newBody = body
        val openBrackets = body.count { it == '{' }
        val closeBrackets = body.count { it == '}' }
        if (openBrackets != closeBrackets) { //some responses don't have closing '}' at the end =/
            newBody += '}'
        }
        return Gson().fromJson(newBody, T::class.java)
    }
}