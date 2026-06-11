package com.sporty.openweather.core.network

/** Outcome of a network call — a success carrying data, or a structured error. */
sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error(
        val message: String,
        val code: Int? = null,
        val throwable: Throwable? = null,
    ) : NetworkResult<Nothing>()
}
