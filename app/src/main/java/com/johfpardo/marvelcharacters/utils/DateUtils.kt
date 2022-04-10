package com.johfpardo.marvelcharacters.utils

import java.util.Date

object DateUtils {
    fun currentTimestamp() = Date().time.toString()
}
