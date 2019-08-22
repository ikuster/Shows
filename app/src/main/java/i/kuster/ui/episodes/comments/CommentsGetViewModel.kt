package i.kuster.ui.episodes.comments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import i.kuster.data.model.CommentGetResponse
import i.kuster.data.repository.EpisodesRepository

class CommentsGetViewModel:ViewModel(),Observer<CommentGetResponse> {
    private val commentsResponseLiveData = MutableLiveData<CommentGetResponse>()

    val liveData: LiveData<CommentGetResponse>
        get() {
            return commentsResponseLiveData
        }
    init {
        EpisodesRepository.liveDataComments().observeForever(this)
    }

    override fun onChanged(comments: CommentGetResponse?) {
       commentsResponseLiveData.value = comments
    }

    fun getComments(id: String) {
        EpisodesRepository.fetchAllComments(id)
    }
    fun restartComments(){
        EpisodesRepository.restartCommentsData()
    }
    override fun onCleared() {
        EpisodesRepository.liveDataComments().removeObserver(this)
    }
}