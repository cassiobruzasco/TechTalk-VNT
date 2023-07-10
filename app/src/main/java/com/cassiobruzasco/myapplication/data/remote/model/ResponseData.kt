package com.cassiobruzasco.myapplication.data.remote.model

sealed class ResponseData<out T : Any> {
    data class Success<out T : Any>(val ret: T) : ResponseData<T>()
    data class Error(val code: Int, val errorResponse: String) : ResponseData<Nothing>()
    data class Exception(val e: Throwable) : ResponseData<Nothing>()
}