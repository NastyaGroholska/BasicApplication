package com.shpp.ahrokholska.basicapplication.data

import java.util.UUID

data class Contact(val picture:String, val name: String, val career: String,
                   val id:Long = UUID.randomUUID().mostSignificantBits)