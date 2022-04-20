package com.johfpardo.marvelcharacters.ui.states

import com.johfpardo.marvelcharacters.data.model.Character

data class CharacterDetailUiState(
    val isLoading: Boolean = false,
    val character: Character? = null,
    val errorMessage: String? = null
)
