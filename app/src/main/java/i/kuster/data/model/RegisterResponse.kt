package i.kuster.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterResponse(
    @Json(name = "data")
    var registration: Register?=null,

    @Transient
    var isSuccessful: Boolean = true,
    @Transient
    var message:String?=null
)