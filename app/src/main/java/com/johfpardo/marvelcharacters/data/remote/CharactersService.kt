package com.johfpardo.marvelcharacters.data.remote

import com.johfpardo.marvelcharacters.data.model.CharacterDataWrapper
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharactersService {

    @GET("/v1/public/characters")
    suspend fun getCharacters(@Query("apikey") apiKey: String,
                              @Query("hash") hash: String,
                              @Query("ts") timestamp: String,
                              @Query("limit") limit: Int,
                              @Query("offset") offset: Int): Response<CharacterDataWrapper>

    @GET("/v1/public/characters/{characterId}")
    suspend fun getCharacterById(@Path("characterId") characterId: String,
                                 @Query("apikey") apiKey: String,
                                 @Query("hash") hash: String,
                                 @Query("ts") timestamp: String): Response<CharacterDataWrapper>
}
