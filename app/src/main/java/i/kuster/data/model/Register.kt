package i.kuster.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Register(
    @Json(name = "type")
    val type: String,

    @Json(name = "email")
    val emailRegistration: String,

    @Json(name = "_id")
    val idRegistration: String)