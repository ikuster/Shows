package i.kuster.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EpisodesResponse(
    @Json(name = "data")
    val episodesList: List<Episodes>? = listOf(),

    @Transient
    var isSuccessful: Boolean? = null,
    @Transient
    var message: String? = null
)