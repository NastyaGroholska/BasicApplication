package com.shpp.ahrokholska.basicapplication.data.utils

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object Parser {
    suspend inline fun <reified T> parseBody(body: String): T = withContext(Dispatchers.Default) {
        var newBody = body
        val openBrackets = body.count { it == '{' }
        val closeBrackets = body.count { it == '}' }
        if (openBrackets != closeBrackets) { //some responses don't have closing '}' at the end =/
            newBody += '}'
        }
        Gson().fromJson(newBody, T::class.java)
    }
}