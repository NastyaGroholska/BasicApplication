package com.shpp.ahrokholska.basicapplication.domain.model

sealed class NetworkResponse<T> {
    data class Success<T>(val data: T) : NetworkResponse<T>()
    class NetworkError<T> : NetworkResponse<T>()
    class InputError<T> : NetworkResponse<T>()
}