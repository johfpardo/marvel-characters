package com.johfpardo.marvelcharacters.data.model

import com.johfpardo.marvelcharacters.utils.UrlUtils

data class Image(
    val path: String?,
    val extension: String?
) {
    val fullPath: String
        get() = UrlUtils.makeSecure("$path.$extension")
}
