package com.sporty.openweather.core.network

import java.io.IOException
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException

/**
 * Safely runs an API call and maps thrown exceptions to a [NetworkResult.Error].
 * Coroutine cancellation is rethrown so structured concurrency is preserved.
 */
suspend inline fun <T> safeApiCall(
    crossinline apiCall: suspend () -> T,
): NetworkResult<T> = try {
    NetworkResult.Success(apiCall())
} catch (e: CancellationException) {
    throw e
} catch (e: HttpException) {
    NetworkResult.Error(
        message = e.message(),
        code = e.code(),
        throwable = e,
    )
} catch (e: IOException) {
    NetworkResult.Error(
        message = "Network error. Please check your connection.",
        throwable = e,
    )
} catch (e: Exception) {
    NetworkResult.Error(
        message = e.localizedMessage ?: "Unexpected error",
        throwable = e,
    )
}
