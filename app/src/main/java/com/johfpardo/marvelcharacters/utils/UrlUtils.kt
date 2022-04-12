package com.johfpardo.marvelcharacters.utils

object UrlUtils {
    fun makeSecure(url: String): String = url.replace("http", "https")
}
