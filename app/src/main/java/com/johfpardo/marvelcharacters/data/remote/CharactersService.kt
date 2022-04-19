package com.johfpardo.marvelcharacters.data.remote

import com.johfpardo.marvelcharacters.data.model.CharacterDataWrapper
import com.johfpardo.marvelcharacters.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharactersService {

    @GET("/v1/public/characters")
    suspend fun getCharacters(
        @Query(Constants.LIMIT_QUERY_PARAM) limit: Int,
        @Query(Constants.OFFSET_QUERY_PARAM) offset: Int
    ): Response<CharacterDataWrapper>

    @GET("/v1/public/characters/{${Constants.CHARACTER_ID_PATH}}")
    suspend fun getCharacterById(@Path(Constants.CHARACTER_ID_PATH) characterId: String): Response<CharacterDataWrapper>
}
