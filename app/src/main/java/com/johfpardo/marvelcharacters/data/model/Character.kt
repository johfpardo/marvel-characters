package com.johfpardo.marvelcharacters.data.model

import com.johfpardo.marvelcharacters.utils.Constants

data class Character(
    /**
     * The unique ID of the character resource.
     */
    val id: Int?,
    /**
     * The name of the character.
     */
    val name: String?,
    /**
     * A short bio or description of the character.
     */
    val description: String?,
    /**
     * The date the resource was most recently modified.
     */
    val modified: String?,
    /**
     * The canonical URL identifier for this resource.
     */
    val resourceURI: String?,
    /**
     * A set of public web site URLs for the resource.
     */
    val urls: List<Url>?,
    /**
     * The representative image for this character.
     */
    val thumbnail: Image?,
    /**
     * A resource list containing comics which feature this character.
     */
    val comics: ListWrapper<ComicSummary>?,
    /**
     * A resource list of stories in which this character appears.
     */
    val stories: ListWrapper<StorySummary>?,
    /**
     * A resource list of events in which this character appears.
     */
    val events: ListWrapper<EventSummary>?,
    /**
     * A resource list of series in which this character appears.
     */
    val series: ListWrapper<SeriesSummary>?
) {
    fun toCharacterSummary(): CharacterSummary {
        return CharacterSummary(
            id ?: Constants.INVALID_ID,
            name.orEmpty(),
            description.orEmpty(),
            thumbnail?.fullPath.orEmpty(),
            getDetailItems()
        )
    }

    private fun getDetailItems(): List<DetailItem> {
        val detailItems = mutableListOf<DetailItem>()
        if (comics?.items?.isNotEmpty() == true) {
            detailItems.add(DetailItem.Title(Constants.COMICS))
            for (comic in comics.items) {
                detailItems.add(DetailItem.Item(comic.name.orEmpty()))
            }
        }
        if (stories?.items?.isNotEmpty() == true) {
            detailItems.add(DetailItem.Title(Constants.STORIES))
            for (story in stories.items) {
                detailItems.add(DetailItem.Item(story.name.orEmpty()))
            }
        }
        if (events?.items?.isNotEmpty() == true) {
            detailItems.add(DetailItem.Title(Constants.EVENTS))
            for (event in events.items) {
                detailItems.add(DetailItem.Item(event.name.orEmpty()))
            }
        }
        if (series?.items?.isNotEmpty() == true) {
            detailItems.add(DetailItem.Title(Constants.SERIES))
            for (seriesItem in series.items) {
                detailItems.add(DetailItem.Item(seriesItem.name.orEmpty()))
            }
        }
        return detailItems
    }
}
