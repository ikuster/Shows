package i.kuster.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import i.kuster.ShowsApp
import i.kuster.data.Api
import i.kuster.data.RetrofitClient
import i.kuster.data.database.Show
import i.kuster.data.database.ShowsDatabase
import i.kuster.data.model.ShowDetailResponse
import i.kuster.data.model.ShowLikeResponse
import i.kuster.data.model.ShowResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val TOKEN_ERROR_MSG = "Your token has expired, press Cancel if you want to check for more shows," +
        "if you press Ok we will log you out and after successfull " +
        "login you can continue with your desired action!"
const val NO_INTERNET_MSG = "Check your internet connection!"

object ShowsRepository {
    private val database: ShowsDatabase = Room.databaseBuilder(
        ShowsApp.instance, ShowsDatabase::class.java, "shows-db"
    )
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

    private val apiService = RetrofitClient.retrofitInstance?.create(Api::class.java)

    private val showResponseLiveData = MutableLiveData<ShowResponse>()

    private val showDetailResponseLiveData = MutableLiveData<ShowDetailResponse>()

    private val showLikeResponseLiveData = MutableLiveData<ShowLikeResponse>()

    fun liveDataShow(): LiveData<ShowResponse> =
        showResponseLiveData

    fun liveDataShowDetails(): LiveData<ShowDetailResponse> =
        showDetailResponseLiveData

    fun liveDataShowLiked(): LiveData<ShowLikeResponse> =
        showLikeResponseLiveData

    fun getLikeStatus(id: String): Int = database.showsDao().getLikeStatus(id, getUser())

    fun insertLike(show: Show) = database.showsDao().insertLike(show)

    fun updateLike(show: Show) = database.showsDao().updateLikeStatus(show.showId, show.userName, show.like)


    fun fetchShowData() {
        apiService?.getAllShows()?.enqueue(object : Callback<ShowResponse> {

            override fun onFailure(call: Call<ShowResponse>, t: Throwable) {
                showResponseLiveData.value = ShowResponse(
                    message = NO_INTERNET_MSG,
                    isSuccessful = null
                )
            }

            override fun onResponse(call: Call<ShowResponse>, response: Response<ShowResponse>) {
                with(response) {
                    if (isSuccessful && body() != null) {
                        showResponseLiveData.value =
                            ShowResponse(
                                showList = body()?.showList,
                                isSuccessful = true
                            )
                    } else {
                        showResponseLiveData.value =
                            ShowResponse(isSuccessful = false)
                    }
                }
            }
        })
    }

    fun fetchShowDetails(id: String) {
        apiService?.getOneShow(id)?.enqueue(object : Callback<ShowDetailResponse> {

            override fun onFailure(call: Call<ShowDetailResponse>, t: Throwable) {
                showDetailResponseLiveData.value =
                    ShowDetailResponse(isSuccessful = false)
            }

            override fun onResponse(call: Call<ShowDetailResponse>, response: Response<ShowDetailResponse>) {
                with(response) {
                    if (isSuccessful && body() != null) {
                        showDetailResponseLiveData.value =
                            ShowDetailResponse(
                                showDetail = body()?.showDetail,
                                isSuccessful = true
                            )
                    } else {
                        showDetailResponseLiveData.value =
                            ShowDetailResponse(isSuccessful = false)
                    }
                }
            }
        })
    }

    fun likeShow(token: String?, id: String) {
        apiService?.likeShow(token, id)?.enqueue(object : Callback<ShowLikeResponse> {

            override fun onFailure(call: Call<ShowLikeResponse>, t: Throwable) {
                showLikeResponseLiveData.postValue(
                    ShowLikeResponse(
                        isSuccessful = null,
                        message = NO_INTERNET_MSG
                    )
                )
            }

            override fun onResponse(call: Call<ShowLikeResponse>, response: Response<ShowLikeResponse>) {
                with(response) {
                    if (isSuccessful && body() != null) {
                        showLikeResponseLiveData.postValue(ShowLikeResponse(isSuccessful = true, type = true))
                        fetchShowDetails(id)
                        val currStatus = getLikeStatus(id)
                        if (currStatus == 0) {
                            insertLike(Show(userName = getUser(), showId = id, like = 1))
                        } else if (currStatus == 2) {
                            updateLike(Show(userName = getUser(), showId = id, like = 1))
                        }
                    } else {
                        showLikeResponseLiveData.postValue(
                            ShowLikeResponse(
                                isSuccessful = false,
                                message = TOKEN_ERROR_MSG
                            )
                        )
                    }
                }
            }
        })
    }

    fun dislikeShow(token: String?, id: String) {
        apiService?.dislikeShow(token, id)?.enqueue(object : Callback<ShowLikeResponse> {

            override fun onFailure(call: Call<ShowLikeResponse>, t: Throwable) {
                showLikeResponseLiveData.postValue(
                    ShowLikeResponse(
                        isSuccessful = null,
                        message = NO_INTERNET_MSG
                    )
                )
            }

            override fun onResponse(call: Call<ShowLikeResponse>, response: Response<ShowLikeResponse>) {
                with(response) {
                    if (isSuccessful && body() != null) {
                        showLikeResponseLiveData.postValue(ShowLikeResponse(isSuccessful = true, type = false))
                        fetchShowDetails(id)
                        val currStatus = getLikeStatus(id)
                        if (currStatus == 0) {
                            insertLike(Show(userName = getUser(), showId = id, like = 2))
                        } else if (currStatus == 1) {
                            updateLike(Show(userName = getUser(), showId = id, like = 2))
                        }
                    } else {
                        showLikeResponseLiveData.postValue(
                            ShowLikeResponse(
                                isSuccessful = false,
                                message = TOKEN_ERROR_MSG
                            )
                        )
                    }
                }
            }
        })
    }
    fun restartLikesLiveData() {
        showLikeResponseLiveData.postValue(null)
    }

    fun restartShowDetails() {
        showDetailResponseLiveData.postValue(null)
    }

    fun getUser() = sharedPreferences.getString(USER, "")
}