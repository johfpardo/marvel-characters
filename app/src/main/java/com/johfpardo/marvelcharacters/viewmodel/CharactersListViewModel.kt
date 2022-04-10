package com.johfpardo.marvelcharacters.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.johfpardo.marvelcharacters.data.repository.CharactersRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class CharactersListViewModel(
    private val charactersRepository: CharactersRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel() {
    fun getCharacters() = liveData(defaultDispatcher) {
        emit(charactersRepository.getCharacters())
    }
}
