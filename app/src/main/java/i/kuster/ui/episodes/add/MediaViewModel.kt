package i.kuster.ui.episodes.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import i.kuster.data.model.MediaResponse
import i.kuster.data.repository.EpisodesRepository
import i.kuster.data.repository.JWT
import i.kuster.data.repository.sharedPreferences
import java.io.File

class MediaViewModel : ViewModel(), Observer<MediaResponse> {

    private val mediaLiveData = MutableLiveData<MediaResponse>()

    val liveData: LiveData<MediaResponse>
        get() {
            return mediaLiveData
        }
    init {
        EpisodesRepository.liveDataMedia().observeForever(this)
    }
    fun uploadPhoto(imageFile: File?) {
        EpisodesRepository.uploadMedia(getToken(), imageFile)
    }
    private fun getToken(): String? {
        return sharedPreferences.getString(JWT, "")
    }

    fun restartData() {
        EpisodesRepository.restartMediaData()
    }
    override fun onChanged(media: MediaResponse?) {
        mediaLiveData.value = media
    }
    override fun onCleared() {
        EpisodesRepository.liveDataMedia().removeObserver(this)
    }
}