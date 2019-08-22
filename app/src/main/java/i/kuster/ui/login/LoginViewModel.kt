package i.kuster.ui.login


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import i.kuster.data.model.LoginResponse
import i.kuster.data.model.UserPost
import i.kuster.data.repository.UsersRepository

class LoginViewModel : ViewModel(), Observer<LoginResponse> {

    private val loginAuthLiveData = MutableLiveData<LoginResponse>()

    val liveData: LiveData<LoginResponse>
        get() {
            return loginAuthLiveData
        }

    init {
        UsersRepository.liveData().observeForever(this)
    }

    override fun onChanged(login: LoginResponse?) {
        loginAuthLiveData.postValue(login)
    }

    override fun onCleared() {
        UsersRepository.liveData().removeObserver(this)
    }
    fun loginUser(user: UserPost,rememberMe: Boolean) {
        UsersRepository.loginUser(user,rememberMe)
    }
}