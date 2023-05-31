package com.shpp.ahrokholska.basicapplication.data.repository.userNetwork

data class UserCredentials(val email: String, val password: String)

data class ResponseTokens(
    val status: String, val code: String, val message: String?,
    val data: DataResponseTokens
)

data class DataResponseTokens(val accessToken: String, val refreshToken: String)


data class ResponseUser(
    val status: String, val code: String, val message: String?,
    val data: DataResponseUser
)

data class DataResponseUser(val user: DataUser)


data class ResponseUserPlusToken(
    val status: String, val code: String, val message: String?,
    val data: DataResponseUserPlusToken
)

data class DataResponseUserPlusToken(
    val user: DataUser, val accessToken: String, val refreshToken: String
)


data class DataUser(
    val id: Long, val name: String, val email: String, val phone: String?, val career: String?,
    val address: String?, val birthday: String?, val facebook: String?, val instagram: String?,
    val twitter: String?, val linkedin: String?, val image: String?, val created_at: String?,
    val updated_at: String?
)