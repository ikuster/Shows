package i.kuster.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommentGet(
    @Json(name = "episodeId")
    var episodeId: String,
    @Json(name = "text")
    var commentContent: String?,
    @Json(name = "userEmail")
    var autorEmail: String,
    @Json(name = "_id")
    var commentId: String?

)