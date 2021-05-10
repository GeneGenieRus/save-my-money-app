package com.genegenie.savemymoney.model

sealed class ResponseWrapper<T>(
    val type: ResponseType,
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : ResponseWrapper<T>(ResponseType.SUCCESS, data)
    class Loading<T>(data: T? = null) : ResponseWrapper<T>(ResponseType.LOADING, data)
    class Error<T>(message: String, data: T? = null) : ResponseWrapper<T>(ResponseType.ERROR, data, message)
}

enum class ResponseType {
    SUCCESS, LOADING, ERROR
}