package i.kuster.ui.shows

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import i.kuster.data.model.ShowResponse
import i.kuster.data.repository.ShowsRepository
import i.kuster.data.repository.UsersRepository


class ShowViewModel : ViewModel(), Observer<ShowResponse> {
    private val showResponseLiveData = MutableLiveData<ShowResponse>()

    val liveData: LiveData<ShowResponse>
        get() {
            return showResponseLiveData
        }

    init {
        ShowsRepository.liveDataShow().observeForever(this)
    }


    fun getShowData() {
        ShowsRepository.fetchShowData()
    }

    fun logOut() {
        UsersRepository.logOutUser()
    }
    override fun onCleared() {
        ShowsRepository.liveDataShow().removeObserver(this)
    }

    override fun onChanged(showResponseData: ShowResponse?) {
        showResponseLiveData.value = showResponseData
    }
}