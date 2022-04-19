package com.johfpardo.marvelcharacters.data.remote

import javax.inject.Inject

class CharactersClient @Inject constructor(
    private val charactersService: CharactersService
) {
    suspend fun getCharacters(limit: Int, offset: Int) =
        charactersService.getCharacters(limit, offset)

    suspend fun getCharacterById(characterId: String) =
        charactersService.getCharacterById(characterId)
}
