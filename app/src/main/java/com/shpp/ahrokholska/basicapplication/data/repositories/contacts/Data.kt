package com.shpp.ahrokholska.basicapplication.data.repositories.contacts

import com.shpp.ahrokholska.basicapplication.data.repositories.userNetwork.DataUser

data class ResponseContacts(
    val status: String, val code: String, val message: String?,
    val data: DataResponseContacts
)

data class DataResponseContacts(val contacts: List<DataUser>)

data class ResponseUsers(
    val status: String, val code: String, val message: String?,
    val data: DataResponseUsers
)

data class DataResponseUsers(val users: List<DataUser>)