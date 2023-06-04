package com.shpp.ahrokholska.basicapplication.data.model

data class ResponseUserPlusToken(
    val status: String, val code: String, val message: String?, val data: Data
) {
    data class Data(val user: DataUser, val accessToken: String, val refreshToken: String)
}
