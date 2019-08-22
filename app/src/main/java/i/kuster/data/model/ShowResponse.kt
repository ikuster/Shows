package i.kuster.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ShowResponse(
    @Json(name = "data")
    val showList: List<Show>? = arrayListOf(),

    @Transient
    var isSuccessful: Boolean? = null,
    @Transient
    var message:String?=null
)