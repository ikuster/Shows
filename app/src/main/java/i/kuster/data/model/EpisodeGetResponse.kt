package i.kuster.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EpisodeGetResponse(
    @Json(name = "data")
    val episode: EpisodeGet? = null,

    @Transient
    var isSuccessful: Boolean? = null,
    @Transient
    var message: String? = null
)