package i.kuster.ui.episodes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import i.kuster.data.model.EpisodesResponse
import i.kuster.data.repository.EpisodesRepository


class EpisodesViewModel : ViewModel(), Observer<EpisodesResponse> {

    private val episodesResponseLiveData = MutableLiveData<EpisodesResponse>()

    val liveData: LiveData<EpisodesResponse>
        get() {
            return episodesResponseLiveData
        }
    init {
        EpisodesRepository.liveData().observeForever(this)
    }

    override fun onChanged(episodes: EpisodesResponse?) {
        episodesResponseLiveData.value = episodes
    }

    fun getEpisodes(id: String) {
        EpisodesRepository.fetchEpisodes(id)
    }
    fun restartEpisodes(){
        EpisodesRepository.restartEpisodes()
    }
    override fun onCleared() {
        EpisodesRepository.liveData().removeObserver(this)
    }
}