package com.shpp.ahrokholska.basicapplication.data.utils

import com.shpp.ahrokholska.basicapplication.domain.model.NetworkResponse
import kotlinx.coroutines.CancellationException

object ErrorHandler {
    fun <T> processError(exception: Exception): NetworkResponse<T> {
        return when (exception) {
            is CancellationException -> throw exception

            is retrofit2.HttpException -> {
                if (Constants.RESPONSE_ERRORS.values().any { it.code == exception.code() }) {
                    NetworkResponse.InputError()
                } else {
                    NetworkResponse.NetworkError()
                }
            }

            else -> {
                NetworkResponse.NetworkError()
            }
        }
    }
}