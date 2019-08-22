package i.kuster.ui.episodes.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import i.kuster.data.model.EpisodePost
import i.kuster.data.model.EpisodePostResponse
import i.kuster.data.repository.EpisodesRepository
import i.kuster.data.repository.JWT
import i.kuster.data.repository.UsersRepository
import i.kuster.data.repository.sharedPreferences


class AddEpisodeViewModel : ViewModel(), Observer<EpisodePostResponse> {
    private val tokenLiveData = MutableLiveData<EpisodePostResponse>()

    val liveData: LiveData<EpisodePostResponse>
        get() {
            return tokenLiveData
        }

    init {
        EpisodesRepository.liveDataToken().observeForever(this)
    }

    fun addEpisode(episode: EpisodePost) {
        EpisodesRepository.addEpisode(getToken(), episode)
    }
    private fun getToken(): String? {
        return sharedPreferences.getString(JWT, "")
    }
    fun restartTokenCondition(){
        EpisodesRepository.removeTokenCondition()
    }
    fun logOut() {
        UsersRepository.logOutUser()
        restartTokenCondition()
    }

    override fun onChanged(tokenCondition: EpisodePostResponse?) {
        tokenLiveData.value = tokenCondition
    }

    override fun onCleared() {
        EpisodesRepository.liveDataToken().removeObserver(this)
    }
}