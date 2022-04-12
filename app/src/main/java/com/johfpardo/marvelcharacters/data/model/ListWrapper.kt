package com.johfpardo.marvelcharacters.data.model

data class ListWrapper<out T>(
    val available: Int?,
    val returned: Int?,
    val collectionURI: String?,
    val items: List<T>
)
