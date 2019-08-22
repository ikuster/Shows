package i.kuster.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MediaResponse(
    @Json(name = "data")
    var media: Media? = null,
    @Transient
    var isSuccessful: Boolean? = null,
    @Transient
    var message: String? = null

)