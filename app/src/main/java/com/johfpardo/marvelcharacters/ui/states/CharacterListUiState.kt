package com.johfpardo.marvelcharacters.ui.states

data class CharacterListUiState(
    val isListEmpty: Boolean = false,
    val isLoading: Boolean = false,
    val isInitialRetry: Boolean = false,
    val errorMessage: String? = null
)
