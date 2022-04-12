package com.johfpardo.marvelcharacters.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.johfpardo.marvelcharacters.data.repository.CharactersRepository
import java.lang.IllegalArgumentException
import javax.inject.Inject

class CharactersListViewModelFactory @Inject constructor(
    private val charactersRepository: CharactersRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CharactersListViewModel::class.java)) {
            return CharactersListViewModel(charactersRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
