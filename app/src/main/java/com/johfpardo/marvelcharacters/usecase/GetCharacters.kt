package com.johfpardo.marvelcharacters.usecase

import com.johfpardo.marvelcharacters.data.repository.CharactersRepository
import javax.inject.Inject

class GetCharacters @Inject constructor(private val charactersRepository: CharactersRepository) {
    fun execute() = charactersRepository.getCharacters()
}
