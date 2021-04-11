package com.cartrack.mobile.codingchallenge.login.model

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class FetchResult<out T : Any> {

    data class Success<out T : Any>(val data: T) : FetchResult<T>()
    data class Failure<out T : Any>(val data: T) : FetchResult<T>()
    data class Error(val exception: Exception) : FetchResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            is Failure -> "$data"
        }
    }
}