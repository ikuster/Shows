package i.kuster.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommentPost(
    @Json(name = "text")
    var commentContent: String,
    @Json(name = "episodeId")
    var episodeId: String?
)