package i.kuster.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommentGetResponse(
    @Json(name = "data")
    val commentsList: List<CommentGet>? = null,
    @Transient
    var isSuccessful: Boolean? = null,
    @Transient
    var message: String? = null
)