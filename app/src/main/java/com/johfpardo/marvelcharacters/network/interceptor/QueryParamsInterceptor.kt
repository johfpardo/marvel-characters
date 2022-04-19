package com.johfpardo.marvelcharacters.network.interceptor

import com.johfpardo.marvelcharacters.utils.Constants
import com.johfpardo.marvelcharacters.utils.DateUtils
import com.johfpardo.marvelcharacters.utils.SecurityUtils
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Add common query parameters for all requests
 */
class QueryParamsInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val timestamp = DateUtils.currentTimestamp()
        val newUrl = chain.request().url().newBuilder()
            .addQueryParameter(Constants.API_KEY_QUERY_PARAM, Constants.API_KEY)
            .addQueryParameter(Constants.HASH_QUERY_PARAM, SecurityUtils.getHash(timestamp))
            .addQueryParameter(Constants.TIMESTAMP_QUERY_PARAM, timestamp)
            .build()
        val request = chain.request().newBuilder().url(newUrl).build()
        return chain.proceed(request)
    }
}
