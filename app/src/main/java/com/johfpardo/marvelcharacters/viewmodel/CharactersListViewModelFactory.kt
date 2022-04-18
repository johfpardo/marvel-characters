package com.johfpardo.marvelcharacters.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.johfpardo.marvelcharacters.usecase.GetCharacters
import javax.inject.Inject

class CharactersListViewModelFactory @Inject constructor(
    private val getCharacters: GetCharacters
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CharactersListViewModel::class.java)) {
            return CharactersListViewModel(getCharacters) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
