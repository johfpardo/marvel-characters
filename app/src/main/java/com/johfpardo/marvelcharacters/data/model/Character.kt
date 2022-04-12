package com.johfpardo.marvelcharacters.data.model

data class Character(
    val id: Int?, // The unique ID of the character resource.,
    val name: String?, // The name of the character.,
    val description: String?, // A short bio or description of the character.,
    val modified: String?, // The date the resource was most recently modified.,
    val resourceURI: String?, // The canonical URL identifier for this resource.,
    //urls (Array[Url], optional): A set of public web site URLs for the resource.,
    val thumbnail: Image? // The representative image for this character.,
    //comics (ComicList, optional): A resource list containing comics which feature this character.,
    //stories (StoryList, optional): A resource list of stories in which this character appears.,
    //events (EventList, optional): A resource list of events in which this character appears.,
    //series (SeriesList, optional):
)
