package com.johfpardo.marvelcharacters.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.johfpardo.marvelcharacters.data.repository.CharacterRepository
import javax.inject.Inject

class CharacterDetailViewModelFactory @Inject constructor(
    private val characterRepository: CharacterRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CharacterDetailViewModel::class.java)) {
            return CharacterDetailViewModel(characterRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
