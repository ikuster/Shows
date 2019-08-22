package i.kuster.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Media(
    @Json(name = "path")
    val mediaPath: String,

    @Json(name = "type")
    val mediaType: String,

    @Json(name = "_id")
    val mediaId: String
)