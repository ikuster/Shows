package i.kuster.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserPost(
    @Json(name="email")
    var userEmail: String,
    @Json(name="password")
    var userPassword: String
)
