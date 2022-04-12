package com.johfpardo.marvelcharacters.data.repository

import com.johfpardo.marvelcharacters.data.model.CharacterDataContainer
import com.johfpardo.marvelcharacters.data.model.Resource
import com.johfpardo.marvelcharacters.data.remote.CharactersClient
import com.johfpardo.marvelcharacters.utils.DateUtils.currentTimestamp
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val charactersClient: CharactersClient
) {
    suspend fun getCharacterById(characterId: String): Resource<CharacterDataContainer> {
        charactersClient.getCharacterById(characterId, currentTimestamp()).apply {
            return if (isSuccessful) {
                Resource.Success(body()?.data)
            } else {
                Resource.Error(errorBody().toString())
            }
        }
    }
}
