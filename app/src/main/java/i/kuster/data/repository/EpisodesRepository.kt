package i.kuster.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import i.kuster.data.Api
import i.kuster.data.RetrofitClient
import i.kuster.data.model.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

object EpisodesRepository {
    private val apiService = RetrofitClient.retrofitInstance?.create(Api::class.java)

    private val episodesResponseLiveData = MutableLiveData<EpisodesResponse>()
    private val episodeResponseLiveData = MutableLiveData<EpisodeGetResponse>()
    private val tokenConditionLiveData = MutableLiveData<EpisodePostResponse>()
    private val mediaResponseLiveData = MutableLiveData<MediaResponse>()
    private val commentsResponseLiveData = MutableLiveData<CommentGetResponse>()
    private val tokenConditionCommentsLiveData = MutableLiveData<CommentPostResponse>()
    private val commentDeleteLiveData = MutableLiveData<CommentDeleteResponse>()


    fun liveData(): LiveData<EpisodesResponse> =
        episodesResponseLiveData

    fun liveDataToken(): LiveData<EpisodePostResponse> =
        tokenConditionLiveData

    fun liveDataEpisode(): LiveData<EpisodeGetResponse> =
        episodeResponseLiveData

    fun liveDataMedia(): LiveData<MediaResponse> =
        mediaResponseLiveData

    fun liveDataComments(): LiveData<CommentGetResponse> =
        commentsResponseLiveData

    fun liveDataTokenComments(): LiveData<CommentPostResponse> =
        tokenConditionCommentsLiveData

    fun liveDataCommentDelete(): LiveData<CommentDeleteResponse> =
        commentDeleteLiveData

    fun fetchEpisodes(id: String) {
        apiService?.getEpisodes(id)?.enqueue(object : Callback<EpisodesResponse> {

            override fun onFailure(call: Call<EpisodesResponse>, t: Throwable) {
                episodesResponseLiveData.value =
                    EpisodesResponse(
                        message = NO_INTERNET_MSG,
                        isSuccessful = null
                    )
            }

            override fun onResponse(call: Call<EpisodesResponse>, response: Response<EpisodesResponse>) {
                with(response) {
                    if (isSuccessful && body() != null) {
                        episodesResponseLiveData.value =
                            EpisodesResponse(
                                episodesList = body()?.episodesList,
                                isSuccessful = true
                            )
                    } else {
                        episodesResponseLiveData.value =
                            EpisodesResponse(isSuccessful = false)
                    }
                }
            }
        })
    }

    fun getEpisode(id: String) {
        apiService?.getEpisode(id)?.enqueue(object : Callback<EpisodeGetResponse> {

            override fun onFailure(call: Call<EpisodeGetResponse>, t: Throwable) {
                episodeResponseLiveData.value =
                    EpisodeGetResponse(
                        message = NO_INTERNET_MSG,
                        isSuccessful = null
                    )
            }

            override fun onResponse(call: Call<EpisodeGetResponse>, response: Response<EpisodeGetResponse>) {
                with(response) {
                    if (isSuccessful && body() != null) {
                        episodeResponseLiveData.value =
                            EpisodeGetResponse(
                                episode = body()?.episode,
                                isSuccessful = true
                            )
                    } else {
                        episodeResponseLiveData.value =
                            EpisodeGetResponse(
                                isSuccessful = false
                            )
                    }
                }
            }
        })
    }

    fun addEpisode(token: String?, episode: EpisodePost) {
        apiService?.createEpisode(token, episode)?.enqueue(object : Callback<EpisodePostResponse> {

            override fun onFailure(call: Call<EpisodePostResponse>, t: Throwable) {
                tokenConditionLiveData.postValue(
                    EpisodePostResponse(
                        isSuccessful = null,
                        message = NO_INTERNET_MSG
                    )
                )
            }

            override fun onResponse(call: Call<EpisodePostResponse>, response: Response<EpisodePostResponse>) {
                with(response) {
                    if (isSuccessful) {
                        tokenConditionLiveData.postValue(
                            EpisodePostResponse(
                                isSuccessful = true,
                                message = null
                            )
                        )
                        fetchEpisodes(episode.showId)

                    } else {
                        tokenConditionLiveData.postValue(
                            EpisodePostResponse(
                                isSuccessful = false,
                                message = TOKEN_ERROR_MSG
                            )
                        )
                    }
                }
            }

        })
    }

    fun uploadMedia(token: String?, imageFile: File?) {
        apiService?.uploadMedia(token, RequestBody.create(MediaType.parse("image/jpg"), imageFile))
            ?.enqueue(object : Callback<MediaResponse> {
                override fun onFailure(call: Call<MediaResponse>, t: Throwable) {
                    mediaResponseLiveData.postValue(
                        MediaResponse(
                            message = NO_INTERNET_MSG,
                            isSuccessful = null
                        )
                    )
                }

                override fun onResponse(call: Call<MediaResponse>, response: Response<MediaResponse>) {
                    with(response) {
                        if (isSuccessful) {
                            mediaResponseLiveData.postValue(
                                MediaResponse(
                                    media = body()?.media,
                                    isSuccessful = true
                                )
                            )
                        } else {
                            mediaResponseLiveData.postValue(
                                MediaResponse(
                                    message = TOKEN_ERROR_MSG,
                                    isSuccessful = false
                                )
                            )
                        }
                    }
                }

            })
    }

    fun fetchAllComments(episodeId: String) {
        apiService?.getComments(episodeId)?.enqueue(object : Callback<CommentGetResponse> {
            override fun onFailure(call: Call<CommentGetResponse>, t: Throwable) {
                commentsResponseLiveData.postValue(
                    CommentGetResponse(
                        isSuccessful = null,
                        message = NO_INTERNET_MSG
                    )
                )

            }

            override fun onResponse(call: Call<CommentGetResponse>, response: Response<CommentGetResponse>) {
                with(response) {
                    if (isSuccessful) {
                        commentsResponseLiveData.postValue(
                            CommentGetResponse(
                                commentsList = body()?.commentsList,
                                isSuccessful = true
                            )
                        )

                    } else {
                        commentsResponseLiveData.postValue(
                            CommentGetResponse(
                                isSuccessful = false
                            )
                        )
                    }
                }
            }

        })
    }

    fun postComment(token: String?, comment: CommentPost) {
        apiService?.postComment(token, comment)?.enqueue(object : Callback<CommentPostResponse> {
            override fun onFailure(call: Call<CommentPostResponse>, t: Throwable) {
                tokenConditionCommentsLiveData.postValue(
                    CommentPostResponse(
                        isSuccessful = null,
                        message = NO_INTERNET_MSG
                    )
                )
            }

            override fun onResponse(call: Call<CommentPostResponse>, response: Response<CommentPostResponse>) {
                with(response) {
                    if (isSuccessful) {
                        tokenConditionCommentsLiveData.postValue(
                            CommentPostResponse(
                                isSuccessful = true
                            )
                        )
                        fetchAllComments(comment.episodeId.toString())

                    } else {
                        tokenConditionCommentsLiveData.postValue(
                            CommentPostResponse(
                                isSuccessful = false,
                                message = TOKEN_ERROR_MSG
                            )
                        )
                    }
                }
            }

        })
    }

    fun deleteComment(token: String?, comment:CommentGet) {
        apiService?.deleteComment(token, comment.commentId.toString())?.enqueue(object : Callback<CommentDeleteResponse> {
            override fun onFailure(call: Call<CommentDeleteResponse>, t: Throwable) {
                commentDeleteLiveData.postValue(CommentDeleteResponse(
                    isSuccessful = null
                ))
                fetchAllComments(comment.episodeId)
            }

            override fun onResponse(call: Call<CommentDeleteResponse>, response: Response<CommentDeleteResponse>) {
                with(response) {
                    if (isSuccessful) {
                        commentDeleteLiveData.postValue(CommentDeleteResponse(
                            isSuccessful = true
                        ))
                        fetchAllComments(comment.episodeId)
                    } else {
                        commentDeleteLiveData.postValue(CommentDeleteResponse(
                            isSuccessful = false,
                            message = TOKEN_ERROR_MSG
                        ))
                    }
                }
            }

        })
    }

    fun removeTokenCondition() {
        tokenConditionLiveData.postValue(
            null
        )
    }

    fun removeTokenComments() {
        tokenConditionCommentsLiveData.postValue(null)
    }

    fun restartMediaData() {
        mediaResponseLiveData.postValue(null)
    }

    fun restartEpisodeDetailsData() {
        episodeResponseLiveData.postValue(null)
    }

    fun restartCommentsData() {
        commentsResponseLiveData.postValue(null)
    }

    fun restartEpisodes() {
        episodesResponseLiveData.postValue(null)
    }
}