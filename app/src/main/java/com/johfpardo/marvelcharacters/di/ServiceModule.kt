package com.johfpardo.marvelcharacters.di

import com.johfpardo.marvelcharacters.data.remote.CharactersService
import com.johfpardo.marvelcharacters.network.interceptor.QueryParamsInterceptor
import com.johfpardo.marvelcharacters.utils.Constants
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ServiceModule {
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient.Builder().addInterceptor(QueryParamsInterceptor()).build())
        .build()

    @Singleton
    @Provides
    fun providesCharactersService(retrofit: Retrofit): CharactersService =
        retrofit.create(CharactersService::class.java)
}
