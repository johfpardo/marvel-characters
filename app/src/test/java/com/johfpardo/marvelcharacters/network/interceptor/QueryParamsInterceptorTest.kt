package com.johfpardo.marvelcharacters.network.interceptor

import com.johfpardo.marvelcharacters.utils.Constants
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

class QueryParamsInterceptorTest {

    @MockK
    private lateinit var chain: Interceptor.Chain

    @MockK
    private lateinit var urlBuilder: HttpUrl.Builder

    @MockK
    private lateinit var url: HttpUrl

    @MockK
    private lateinit var request: Request

    @MockK
    private lateinit var response: Response

    @InjectMockKs
    private lateinit var queryParamsInterceptor: QueryParamsInterceptor

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun intercept_addQueryParamsSuccessfully() {
        //Given
        every { chain.request().url().newBuilder() } returns urlBuilder
        every { urlBuilder.addQueryParameter(any(), any()) } returns urlBuilder
        every { urlBuilder.build() } returns url
        every { chain.request().newBuilder().url(any<HttpUrl>()).build() } returns request
        every { chain.proceed(any()) } returns response

        //When
        val result = queryParamsInterceptor.intercept(chain)
        //Then
        assertThat(result, `is`(response))
        verify { urlBuilder.addQueryParameter(Constants.API_KEY_QUERY_PARAM, Constants.API_KEY) }
        verify { urlBuilder.addQueryParameter(Constants.HASH_QUERY_PARAM, any()) }
        verify { urlBuilder.addQueryParameter(Constants.TIMESTAMP_QUERY_PARAM, any()) }
    }
}
