package i.kuster.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Show(
    @Json(name = "_id")
    val showId: String,

    @Json(name = "title")
    val showTitle: String,

    @Json(name = "imageUrl")
    val showImage: String,

    @Json(name = "likesCount")
    val showLikes: Int
)