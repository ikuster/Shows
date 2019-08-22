package i.kuster.ui.shows

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import i.kuster.data.model.ShowLikeResponse
import i.kuster.data.repository.JWT
import i.kuster.data.repository.ShowsRepository
import i.kuster.data.repository.UsersRepository
import i.kuster.data.repository.sharedPreferences

class ShowLikeViewModel : ViewModel(), Observer<ShowLikeResponse> {
    private val showLikedLiveData= MutableLiveData<ShowLikeResponse>()


    val liveData:LiveData<ShowLikeResponse>
        get(){
            return showLikedLiveData
        }

    init {
        ShowsRepository.liveDataShowLiked().observeForever(this)
    }



    fun likeShow(showId: String) {
        ShowsRepository.likeShow(getToken(), showId)
    }

    fun dislikeShow(showId: String) {
        ShowsRepository.dislikeShow(getToken(), showId)
    }

    fun getLikeStatus(showId: String):Int{
        return ShowsRepository.getLikeStatus(showId)
    }
    fun restartLiveData(){
        ShowsRepository.restartLikesLiveData()
    }
    fun logOut() {
        UsersRepository.logOutUser()
        ShowsRepository.restartLikesLiveData()
    }

    private fun getToken(): String? {
        return sharedPreferences.getString(JWT, "")
    }

    override fun onCleared() {
        ShowsRepository.liveDataShowLiked().removeObserver(this)
    }


    override fun onChanged(showLikeData: ShowLikeResponse?) {
        showLikedLiveData.value = showLikeData
    }
}