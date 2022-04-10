package com.johfpardo.marvelcharacters.data.repository

import com.johfpardo.marvelcharacters.data.remote.CharactersClient
import java.util.Date
import javax.inject.Inject

class CharactersRepository @Inject constructor(private val charactersClient: CharactersClient) {

    suspend fun getCharacters() = charactersClient.getCharacters(currentTimestamp())

    private fun currentTimestamp() = Date().time.toString()
}
