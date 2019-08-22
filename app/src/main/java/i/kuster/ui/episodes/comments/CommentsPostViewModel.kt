package i.kuster.ui.episodes.comments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import i.kuster.data.model.CommentPost
import i.kuster.data.model.CommentPostResponse
import i.kuster.data.repository.EpisodesRepository
import i.kuster.data.repository.JWT
import i.kuster.data.repository.UsersRepository
import i.kuster.data.repository.sharedPreferences

class CommentsPostViewModel:ViewModel(),Observer<CommentPostResponse> {
    private val tokenLiveData = MutableLiveData<CommentPostResponse>()

    val liveData: LiveData<CommentPostResponse>
        get() {
            return tokenLiveData
        }
    init {
        EpisodesRepository.liveDataTokenComments().observeForever(this)
    }

    override fun onChanged(comment: CommentPostResponse?) {
        tokenLiveData.value = comment
    }

    fun postComment(comment: CommentPost){
        EpisodesRepository.postComment(getToken(),comment)
    }

    fun getToken():String?{
        return sharedPreferences.getString(JWT,"")
    }
    fun restartTokenCondition(){
        EpisodesRepository.removeTokenComments()
    }
    fun logOut() {
        UsersRepository.logOutUser()
        restartTokenCondition()
    }

    override fun onCleared() {
        EpisodesRepository.liveDataTokenComments().removeObserver(this)
    }
}