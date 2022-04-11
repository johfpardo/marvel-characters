package com.johfpardo.marvelcharacters.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.johfpardo.marvelcharacters.data.model.Character
import com.johfpardo.marvelcharacters.data.remote.CharactersClient
import com.johfpardo.marvelcharacters.data.repository.paging.CharactersPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CharactersRepository @Inject constructor(private val charactersClient: CharactersClient) {

    fun getCharacters(): Flow<PagingData<Character>> {
        return Pager(
            PagingConfig(NETWORK_PAGE_SIZE),
            pagingSourceFactory = { CharactersPagingSource(charactersClient) }
        ).flow
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 20
    }
}
