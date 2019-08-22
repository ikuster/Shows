package i.kuster.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import i.kuster.data.model.RegisterResponse
import i.kuster.data.model.UserPost
import i.kuster.data.repository.UsersRepository

class RegisterViewModel : ViewModel(), Observer<RegisterResponse> {

    private val regUserLiveData = MutableLiveData<RegisterResponse>()

    val liveData: LiveData<RegisterResponse>
        get() {
            return regUserLiveData
        }

    init {
        UsersRepository.liveDataReg().observeForever(this)
    }

    fun createUser(user: UserPost) {
        UsersRepository.createUser(user)
    }

    override fun onChanged(register: RegisterResponse?) {
        regUserLiveData.postValue(register)
    }

    override fun onCleared() {
        UsersRepository.liveDataReg().removeObserver(this)
    }
}