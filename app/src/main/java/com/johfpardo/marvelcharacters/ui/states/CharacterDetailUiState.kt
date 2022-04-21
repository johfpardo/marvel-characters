package com.johfpardo.marvelcharacters.ui.states

import com.johfpardo.marvelcharacters.data.model.CharacterSummary

data class CharacterDetailUiState(
    val isLoading: Boolean = false,
    val character: CharacterSummary? = null,
    val errorMessage: String? = null
)
