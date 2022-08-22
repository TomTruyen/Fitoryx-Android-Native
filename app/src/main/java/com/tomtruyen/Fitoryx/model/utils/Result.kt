package com.tomtruyen.Fitoryx.model.utils

sealed class Result<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T? = null) : Result<T>(data)
    class Error<T>(message: String, data: T? = null) : Result<T>(data, message)
}