package com.johfpardo.marvelcharacters.data.model

data class CharacterSummary(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnailPath: String,
    val detailItems: List<DetailItem>
)
