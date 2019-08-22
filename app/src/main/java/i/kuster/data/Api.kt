package i.kuster.data

import i.kuster.data.model.*
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface Api {
    @GET("shows/")
    fun getAllShows(): Call<ShowResponse>

    @GET("shows/{showId}")
    fun getOneShow(@Path("showId") idShow: String): Call<ShowDetailResponse>

    @GET("shows/{showId}/episodes")
    fun getEpisodes(@Path("showId") idShow: String): Call<EpisodesResponse>

    @GET("episodes/{episodeId}")
    fun getEpisode(@Path("episodeId") idEpisode: String): Call<EpisodeGetResponse>

    @GET("episodes/{episodeId}/comments")
    fun getComments(@Path("episodeId") idEpisode: String): Call<CommentGetResponse>

    @POST("users/")
    fun createUser(@Body user: UserPost): Call<RegisterResponse>

    @POST("users/sessions")
    fun loginUser(@Body user: UserPost): Call<LoginResponse>

    @POST("episodes")
    fun createEpisode(@Header("Authorization") authorization: String?, @Body episode: EpisodePost): Call<EpisodePostResponse>

    @POST("shows/{showId}/like")
    fun likeShow(@Header("Authorization") authorization: String?, @Path("showId") idShow: String): Call<ShowLikeResponse>

    @POST("shows/{showId}/dislike")
    fun dislikeShow(@Header("Authorization") authorization: String?, @Path("showId") idShow: String): Call<ShowLikeResponse>

    @Multipart
    @POST("media/")
    fun uploadMedia(@Header("Authorization") authorization: String?,@Part("file\"; filename=\"image.jpg\" ") request:RequestBody) :Call<MediaResponse>

    @POST("comments/")
    fun postComment(@Header("Authorization") authorization: String?,@Body comment:CommentPost): Call<CommentPostResponse>

    @DELETE("comments/{commentId}")
    fun deleteComment(@Header("Authorization") authorization: String?, @Path("commentId") idComment: String): Call<CommentDeleteResponse>
}