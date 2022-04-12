package com.johfpardo.marvelcharacters.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.johfpardo.marvelcharacters.data.model.CharacterDataContainer
import com.johfpardo.marvelcharacters.data.model.Resource
import com.johfpardo.marvelcharacters.data.repository.CharacterRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class CharacterDetailViewModel(
    private val characterRepository: CharacterRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    fun getCharacterById(characterId: String) =
        liveData<Resource<CharacterDataContainer>>(defaultDispatcher) {
            emit(Resource.Loading())
            try {
                emit(characterRepository.getCharacterById(characterId))
            } catch (exception: Exception) {
                emit(Resource.Error(exception.localizedMessage))
            }
        }
}
