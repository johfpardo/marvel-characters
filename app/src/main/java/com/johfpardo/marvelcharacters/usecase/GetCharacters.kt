package com.johfpardo.marvelcharacters.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.johfpardo.marvelcharacters.data.model.CharacterSummary
import com.johfpardo.marvelcharacters.data.repository.CharactersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCharacters @Inject constructor(private val charactersRepository: CharactersRepository) {
    fun execute(): Flow<PagingData<CharacterSummary>> = charactersRepository.getCharacters()
        .map { pagingData -> pagingData.map { it.toCharacterSummary() } }
}
