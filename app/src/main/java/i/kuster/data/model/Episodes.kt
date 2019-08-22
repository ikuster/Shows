package i.kuster.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Episodes(
    @Json(name = "_id")
    val episodeId: String,

    @Json(name = "title")
    val episodeTitle: String,

    @Json(name = "description")
    val episodeDescription: String,

    @Json(name = "imageUrl")
    val episodeImage: String,

    @Json(name = "episodeNumber")
    val episodeNumber: String,

    @Json(name = "season")
    val episodeSeason: String
)