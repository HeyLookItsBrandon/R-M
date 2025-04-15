package com.coalminesoftware.rickandmorty.data

/** Represents a network request's state, along with data relevant to that state. */
sealed class Resource<out T> {
    object Loading : Resource<Nothing>()
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val message: String, val error: Throwable) : Resource<Nothing>()
}
