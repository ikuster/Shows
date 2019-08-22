package i.kuster.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EpisodePostResponse(
    @Transient
    var isSuccessful: Boolean? = null,
    @Transient
    var message:String?=null
)