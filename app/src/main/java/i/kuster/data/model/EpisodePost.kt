package i.kuster.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EpisodePost(
    @Json(name = "showId")
    var showId: String,
    @Json(name = "mediaId")
    var mediaId: String?,
    @Json(name = "title")
    var title: String,
    @Json(name = "description")
    var description: String,
    @Json(name = "episodeNumber")
    var episodeNumber: String,
    @Json(name = "season")
    var season: String
)
