package com.johfpardo.marvelcharacters.data.model

sealed class DetailItem(val text: String) {
    class Title(text: String): DetailItem(text)
    class Item(text: String): DetailItem(text)
}
