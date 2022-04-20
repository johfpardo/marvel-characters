package com.johfpardo.marvelcharacters.ui.states

data class CharacterItemLoadState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = null
)
