package com.johfpardo.marvelcharacters.data.repository

import com.johfpardo.marvelcharacters.data.model.CharacterDataContainer
import com.johfpardo.marvelcharacters.data.model.Resource
import com.johfpardo.marvelcharacters.data.remote.CharactersClient
import com.johfpardo.marvelcharacters.utils.DateUtils.currentTimestamp
import javax.inject.Inject

class CharactersRepository @Inject constructor(private val charactersClient: CharactersClient) {

    suspend fun getCharacters(): Resource<CharacterDataContainer> {
        charactersClient.getCharacters(currentTimestamp()).apply {
            return if (isSuccessful) {
                Resource.Success(body()?.data)
            } else {
                Resource.Error(errorBody().toString())
            }
        }
    }
}
