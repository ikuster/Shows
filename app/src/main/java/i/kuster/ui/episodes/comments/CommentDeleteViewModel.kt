package i.kuster.ui.episodes.comments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import i.kuster.data.model.CommentDeleteResponse
import i.kuster.data.model.CommentGet
import i.kuster.data.repository.EpisodesRepository
import i.kuster.data.repository.JWT
import i.kuster.data.repository.sharedPreferences

class CommentDeleteViewModel : ViewModel(), Observer<CommentDeleteResponse> {
    private val commentsDeleteLiveData = MutableLiveData<CommentDeleteResponse>()

    val liveData: LiveData<CommentDeleteResponse>
        get() {
            return commentsDeleteLiveData
        }

    init {
        EpisodesRepository.liveDataCommentDelete().observeForever(this)
    }

    fun deleteComment(comment: CommentGet) {
        EpisodesRepository.deleteComment(getToken(), comment)
    }

    fun getToken(): String? {
        return sharedPreferences.getString(JWT, "")
    }

    override fun onChanged(comment: CommentDeleteResponse?) {
        commentsDeleteLiveData.value = comment
    }

    override fun onCleared() {
        EpisodesRepository.liveDataCommentDelete().removeObserver(this)
    }
}