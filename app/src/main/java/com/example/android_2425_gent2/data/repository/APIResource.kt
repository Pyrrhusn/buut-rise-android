package com.example.android_2425_gent2.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed class APIResource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Loading<T>(data: T? = null) : APIResource<T>(data)
    class Success<T>(data: T?) : APIResource<T>(data)
    class Error<T>(message: String, data: T? = null) : APIResource<T>(data, message)
}

fun <T> Flow<T>.asAPIResource(): Flow<APIResource<T>> {
    return this
        .map<T, APIResource<T>> {
            APIResource.Success(it)
        }
        .onStart {
            emit(APIResource.Loading())
        }
        .catch { e ->
            emit(APIResource.Error(e.message ?: "Er is iets misgelopen"))
        }
}