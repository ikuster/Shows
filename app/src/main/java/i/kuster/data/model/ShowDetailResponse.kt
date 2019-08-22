package i.kuster.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ShowDetailResponse(
    @Json(name = "data")
    var showDetail: ShowDetail? = null,

    @Transient
    var isSuccessful: Boolean? = null,
    @Transient
    var message: String? = null
)