package com.johfpardo.marvelcharacters.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.johfpardo.marvelcharacters.usecase.GetCharacterById
import javax.inject.Inject

class CharacterDetailViewModelFactory @Inject constructor(
    private val getCharacterById: GetCharacterById
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CharacterDetailViewModel::class.java)) {
            return CharacterDetailViewModel(getCharacterById) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
