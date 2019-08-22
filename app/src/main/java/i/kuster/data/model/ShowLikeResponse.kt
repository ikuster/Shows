package i.kuster.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ShowLikeResponse(
    @Transient
    var type:Boolean?=null,
    @Transient
    var message:String?=null,
    @Transient
    var isSuccessful: Boolean? = true
)