package com.shpp.ahrokholska.basicapplication.domain.model

data class User(
    val id: Long = 0,
    val name: String? = "",
    val career: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val birthday: String? = null,
    val accessToken: String = "",
    val refreshToken: String = ""
)