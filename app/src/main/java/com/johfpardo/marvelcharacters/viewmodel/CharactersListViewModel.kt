package com.johfpardo.marvelcharacters.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.johfpardo.marvelcharacters.data.model.CharacterDataContainer
import com.johfpardo.marvelcharacters.data.model.Resource
import com.johfpardo.marvelcharacters.data.repository.CharactersRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class CharactersListViewModel(
    private val charactersRepository: CharactersRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    fun getCharacters() = liveData<Resource<CharacterDataContainer>>(defaultDispatcher) {
        emit(Resource.Loading())
        try {
            emit(charactersRepository.getCharacters())
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message))
        }
    }
}
