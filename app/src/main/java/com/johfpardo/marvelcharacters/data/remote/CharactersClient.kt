package com.johfpardo.marvelcharacters.data.remote

import com.johfpardo.marvelcharacters.utils.Constants
import com.johfpardo.marvelcharacters.utils.SecurityUtils
import javax.inject.Inject

class CharactersClient @Inject constructor(
    private val charactersService: CharactersService
) {
    suspend fun getCharacters(timestamp: String, limit: Int, offset: Int) =
        charactersService.getCharacters(
            Constants.API_KEY,
            SecurityUtils.getHash(timestamp),
            timestamp,
            limit,
            offset
        )

    suspend fun getCharacterById(characterId: String, timestamp: String) =
        charactersService.getCharacterById(
            characterId,
            Constants.API_KEY,
            SecurityUtils.getHash(timestamp),
            timestamp
        )
}
