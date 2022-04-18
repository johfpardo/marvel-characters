package com.johfpardo.marvelcharacters.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.johfpardo.marvelcharacters.usecase.GetCharacters
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class CharactersListViewModel(
    private val getCharacters: GetCharacters,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    fun getCharacters() = getCharacters.execute().asLiveData(defaultDispatcher)
}
