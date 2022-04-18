package com.johfpardo.marvelcharacters.usecase

import com.johfpardo.marvelcharacters.data.repository.CharacterRepository
import javax.inject.Inject

class GetCharacterById @Inject constructor(private val characterRepository: CharacterRepository) {
    suspend fun execute(characterId: String) = characterRepository.getCharacterById(characterId)
}
