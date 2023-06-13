package com.shpp.ahrokholska.basicapplication.data.model

data class ResponseTokens(
    val status: String, val code: String, val message: String?, val data: Data
) {
    data class Data(val accessToken: String, val refreshToken: String)
}
