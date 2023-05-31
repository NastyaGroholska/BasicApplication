package com.shpp.ahrokholska.basicapplication.domain.model

data class NetworkResponse<T>(val code: NetworkResponseCode, val data: T)