package com.johfpardo.marvelcharacters.data.model

data class Character(
    val id: Int?, // The unique ID of the character resource.,
    val name: String?, // The name of the character.,
    val description: String?, // A short bio or description of the character.,
    val modified: String?, // The date the resource was most recently modified.,
    val resourceURI: String?, // The canonical URL identifier for this resource.,
    val urls: List<Url>?, // A set of public web site URLs for the resource.,
    val thumbnail: Image?, // The representative image for this character.,
    val comics: ListWrapper<ComicSummary>?, // A resource list containing comics which feature this character.,
    val stories: ListWrapper<StorySummary>?, // A resource list of stories in which this character appears.,
    val events: ListWrapper<EventSummary>?, // A resource list of events in which this character appears.,
    val series: ListWrapper<SeriesSummary>? // A resource list of series in which this character appears.
) {
    fun getDetailItems(): List<DetailItem> {
        val detailItems = mutableListOf<DetailItem>()
        if (comics?.items?.isNotEmpty() == true) {
            detailItems.add(DetailItem.Title("Comics"))
            for (comic in comics.items) {
                detailItems.add(DetailItem.Item(comic.name.orEmpty()))
            }
        }
        if (stories?.items?.isNotEmpty() == true) {
            detailItems.add(DetailItem.Title("Stories"))
            for (story in stories.items) {
                detailItems.add(DetailItem.Item(story.name.orEmpty()))
            }
        }
        if (events?.items?.isNotEmpty() == true) {
            detailItems.add(DetailItem.Title("Events"))
            for (event in events.items) {
                detailItems.add(DetailItem.Item(event.name.orEmpty()))
            }
        }
        if (series?.items?.isNotEmpty() == true) {
            detailItems.add(DetailItem.Title("Series"))
            for (seriesItem in series.items) {
                detailItems.add(DetailItem.Item(seriesItem.name.orEmpty()))
            }
        }
        return detailItems
    }
}
