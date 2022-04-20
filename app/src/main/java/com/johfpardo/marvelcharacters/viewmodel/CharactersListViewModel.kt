package com.johfpardo.marvelcharacters.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.johfpardo.marvelcharacters.ui.states.CharacterListUiState
import com.johfpardo.marvelcharacters.usecase.GetCharacters
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class CharactersListViewModel(
    private val getCharacters: GetCharacters,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    private val _uiState = MutableLiveData<CharacterListUiState>()

    val uiState: LiveData<CharacterListUiState> = _uiState

    fun getCharacters() = getCharacters.execute().asLiveData(defaultDispatcher)

    fun handleLoadState(loadState: CombinedLoadStates, itemCount: Int) {
        _uiState.value = CharacterListUiState(
            loadState.refresh is LoadState.NotLoading && itemCount == 0,
            loadState.source.refresh is LoadState.Loading,
            loadState.source.refresh is LoadState.Error,
            evaluateError(loadState)
        )
    }

    private fun evaluateError(loadState: CombinedLoadStates): String? {
        val errorState = loadState.source.append as? LoadState.Error
            ?: loadState.source.prepend as? LoadState.Error
            ?: loadState.append as? LoadState.Error
            ?: loadState.prepend as? LoadState.Error
            ?: loadState.refresh as? LoadState.Error
        return errorState?.error?.localizedMessage
    }
}
