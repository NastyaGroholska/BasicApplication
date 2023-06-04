package com.shpp.ahrokholska.basicapplication.data.model

data class ResponseUser(
    val status: String, val code: String, val message: String?, val data: Data
) {
    data class Data(val user: DataUser)
}
