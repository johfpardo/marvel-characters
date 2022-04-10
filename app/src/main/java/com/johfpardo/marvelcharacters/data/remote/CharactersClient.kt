package com.johfpardo.marvelcharacters.data.remote

import com.johfpardo.marvelcharacters.utils.Constants
import com.johfpardo.marvelcharacters.utils.SecurityUtils
import javax.inject.Inject

class CharactersClient @Inject constructor(
    private val charactersService: CharactersService
) {
    suspend fun getCharacters(timestamp: String) =
        charactersService.getCharacters(Constants.API_KEY, SecurityUtils.getHash(timestamp), timestamp)
}
