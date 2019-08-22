package i.kuster.ui.shows

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import i.kuster.data.model.ShowDetailResponse
import i.kuster.data.model.ShowLikeResponse
import i.kuster.data.repository.ShowsRepository

class ShowDetailsViewModel : ViewModel(), Observer<ShowDetailResponse> {
    private val showDetailResponseLiveData = MutableLiveData<ShowDetailResponse>()

    private val showLikedLiveData= MutableLiveData<ShowLikeResponse>()

    val liveData: LiveData<ShowDetailResponse>
        get() {
            return showDetailResponseLiveData
        }

    init {
        ShowsRepository.liveDataShowDetails().observeForever(this)
    }

    fun getShowDetails(id: String) {
        ShowsRepository.fetchShowDetails(id)
    }
    fun restartShowDetails(){
        ShowsRepository.restartShowDetails()
    }
    override fun onCleared() {
        ShowsRepository.liveDataShowDetails().removeObserver(this)
    }

    override fun onChanged(showResponseData: ShowDetailResponse?) {
        showDetailResponseLiveData.value = showResponseData
    }
}