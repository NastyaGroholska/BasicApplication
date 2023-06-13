package com.shpp.ahrokholska.basicapplication.data.model

data class ResponseUsers(
    val status: String, val code: String, val message: String?, val data: Data
) {
    data class Data(val users: List<DataUser>)
}
