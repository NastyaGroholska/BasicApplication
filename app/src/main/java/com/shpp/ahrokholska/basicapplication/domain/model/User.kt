package com.shpp.ahrokholska.basicapplication.domain.model

data class User(
    val id: Long, val name: String, val career: String?, val phone: String?, val address: String?,
    val birthday: String?, val accessToken: String, val refreshToken: String
)