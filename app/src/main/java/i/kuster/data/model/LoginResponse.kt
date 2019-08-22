package i.kuster.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginResponse(
    @Json(name = "data")
    var login: Login?=null,

    @Transient
    var isSuccessful: Boolean? = null,
    @Transient
    var message:String?=null
)