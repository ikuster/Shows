package i.kuster.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ShowDetail(
    @Json(name = "type")
    val showType: String,

    @Json(name = "title")
    val showTitle: String,

    @Json(name = "description")
    val showDescription: String,

    @Json(name = "_id")
    val showId: String,

    @Json(name = "likesCount")
    val showLikes: Int,

    @Json(name = "imageUrl")
    val showImage: String
)