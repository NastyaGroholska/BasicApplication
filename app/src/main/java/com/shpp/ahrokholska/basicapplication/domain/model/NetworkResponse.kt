package com.shpp.ahrokholska.basicapplication.domain.model

sealed class NetworkResponse<T>
data class SuccessNetworkResponse<T>(val data: T) : NetworkResponse<T>()
class NetworkErrorNetworkResponse<T> : NetworkResponse<T>()
class InputErrorNetworkResponse<T> : NetworkResponse<T>()