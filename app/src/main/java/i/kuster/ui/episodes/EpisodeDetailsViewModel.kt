package i.kuster.ui.episodes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import i.kuster.data.model.EpisodeGetResponse
import i.kuster.data.repository.EpisodesRepository

class EpisodeDetailsViewModel : ViewModel(), Observer<EpisodeGetResponse> {

    private val episodeResponseLiveData = MutableLiveData<EpisodeGetResponse>()

    val liveData: LiveData<EpisodeGetResponse>
        get() {
            return episodeResponseLiveData
        }
    init {
        EpisodesRepository.liveDataEpisode().observeForever(this)
    }

    override fun onChanged(episodes: EpisodeGetResponse?) {
        episodeResponseLiveData.value = episodes
    }

    fun getEpisode(id: String) {
        EpisodesRepository.getEpisode(id)
    }

    fun restartLiveData(){
        EpisodesRepository.restartEpisodeDetailsData()
    }
    override fun onCleared() {
        EpisodesRepository.liveDataEpisode().removeObserver(this)
    }
}