package com.johfpardo.marvelcharacters.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.johfpardo.marvelcharacters.data.model.CharacterDataContainer
import com.johfpardo.marvelcharacters.data.model.Resource
import com.johfpardo.marvelcharacters.usecase.GetCharacterById
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class CharacterDetailViewModel(
    private val getCharacterById: GetCharacterById,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    fun getCharacterById(characterId: String) =
        liveData<Resource<CharacterDataContainer>>(defaultDispatcher) {
            emit(Resource.Loading())
            try {
                emit(getCharacterById.execute(characterId))
            } catch (exception: Exception) {
                emit(Resource.Error(exception.localizedMessage))
            }
        }
}
