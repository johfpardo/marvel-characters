package com.johfpardo.marvelcharacters.data.model

sealed class Resource<out T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?) : Resource<T>(data)
    class Error<T>(message: String? = null) : Resource<T>(message = message)
}
