package com.johfpardo.marvelcharacters.usecase

import com.johfpardo.marvelcharacters.data.model.CharacterSummary
import com.johfpardo.marvelcharacters.data.model.Resource
import com.johfpardo.marvelcharacters.data.repository.CharacterRepository
import javax.inject.Inject

class GetCharacterById @Inject constructor(private val characterRepository: CharacterRepository) {
    suspend fun execute(characterId: String): Resource<CharacterSummary> =
        characterRepository.getCharacterById(characterId).run {
            return@run when (this) {
                is Resource.Success -> {
                    Resource.Success(data?.results?.get(0)?.toCharacterSummary())
                }
                is Resource.Error -> {
                    Resource.Error(message)
                }
            }
        }
}
