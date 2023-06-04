package com.shpp.ahrokholska.basicapplication.data.model

import com.shpp.ahrokholska.basicapplication.domain.model.Contact
import com.shpp.ahrokholska.basicapplication.domain.model.User

data class DataUser(
    val id: Long, val name: String?, val email: String, val phone: String?, val career: String?,
    val address: String?, val birthday: String?, val facebook: String?, val instagram: String?,
    val twitter: String?, val linkedin: String?, val image: String?, val created_at: String?,
    val updated_at: String?
) {
    fun toContact(): Contact = Contact(id, name, career, address)
    fun toUser(accessToken: String, refreshToken: String): User =
        User(id, name, career, phone, address, birthday, accessToken, refreshToken)
}