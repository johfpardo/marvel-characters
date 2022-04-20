package com.johfpardo.marvelcharacters.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.johfpardo.marvelcharacters.data.model.Resource
import com.johfpardo.marvelcharacters.ui.states.CharacterDetailUiState
import com.johfpardo.marvelcharacters.usecase.GetCharacterById
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CharacterDetailViewModel(
    private val getCharacterById: GetCharacterById,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    private val _uiState = MutableLiveData<CharacterDetailUiState>()

    val uiState: LiveData<CharacterDetailUiState> = _uiState

    fun getCharacterById(characterId: String) {
        _uiState.value = CharacterDetailUiState(isLoading = true)
        viewModelScope.launch(defaultDispatcher) {
            try {
                when (val result = getCharacterById.execute(characterId)) {
                    is Resource.Success -> {
                        _uiState.postValue(
                            CharacterDetailUiState(character = result.data?.results?.get(0))
                        )
                    }
                    is Resource.Error -> {
                        _uiState.postValue(CharacterDetailUiState(errorMessage = result.message))
                    }
                }
            } catch (exception: Exception) {
                _uiState.postValue(CharacterDetailUiState(errorMessage = exception.localizedMessage))
            }
        }
    }
}
