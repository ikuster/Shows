package i.kuster.data.repository

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import i.kuster.data.Api
import i.kuster.data.RetrofitClient
import i.kuster.data.model.LoginResponse
import i.kuster.data.model.RegisterResponse
import i.kuster.data.model.UserPost
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

lateinit var sharedPreferences: SharedPreferences
lateinit var sharedPreferencesEditor: SharedPreferences.Editor
const val KEY_REMEMBER = "KEY"
const val PREF_NAME = "LOGIN_PREF"
const val JWT = "TOKEN"
const val USER="USERNAME"

object UsersRepository {
    private val apiService = RetrofitClient.retrofitInstance?.create(Api::class.java)

    private val loginAuthLiveData = MutableLiveData<LoginResponse>()

    private val regUserLiveData = MutableLiveData<RegisterResponse>()

    fun liveData(): LiveData<LoginResponse> =
        loginAuthLiveData

    fun liveDataReg(): LiveData<RegisterResponse> =
        regUserLiveData

    fun loginUser(user: UserPost, rememberMe: Boolean) {
        apiService?.loginUser(user)?.enqueue(object : Callback<LoginResponse> {

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                loginAuthLiveData.postValue(
                    LoginResponse(
                        isSuccessful = false,
                        message = NO_INTERNET_MSG
                    )
                )
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                with(response) {
                    if (isSuccessful && body() != null) {
                        loginAuthLiveData.postValue(
                            LoginResponse(
                                login = body()?.login,
                                isSuccessful = true,
                                message = ""
                            )
                        )
                        sharedPreferencesEditor = sharedPreferences.edit()
                        sharedPreferencesEditor.putString(JWT, body()?.login?.token)
                        sharedPreferencesEditor.putString(USER,user.userEmail)
                        sharedPreferencesEditor.apply()
                        if (rememberMe) {
                            setRememberMe(rememberMe = true)
                        }
                    } else {
                        loginAuthLiveData.postValue(
                            LoginResponse(
                                isSuccessful = false,
                                message = "This user does not exist, please try again."
                            )
                        )
                    }
                }
            }
        })
    }

    fun createUser(user: UserPost) {
        apiService?.createUser(user)?.enqueue(object : Callback<RegisterResponse> {
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                regUserLiveData.postValue(
                    RegisterResponse(
                        isSuccessful = false,
                        message = NO_INTERNET_MSG
                    )
                )
            }

            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                with(response) {
                    if (isSuccessful && body() != null) {
                        regUserLiveData.postValue(
                            RegisterResponse(
                                registration = body()?.registration,
                                isSuccessful = true

                            )
                        )
                    } else {
                        regUserLiveData.postValue(
                            RegisterResponse(
                                isSuccessful = false
                            )
                        )
                    }
                }
            }
        })
    }

    fun logOutUser() {
        loginAuthLiveData.postValue(
            LoginResponse(
                login = null,
                isSuccessful = null
            )
        )
        setRememberMe(rememberMe = false)
    }

    private fun setRememberMe(rememberMe: Boolean) {
        sharedPreferencesEditor = sharedPreferences.edit()
        sharedPreferencesEditor.putBoolean(KEY_REMEMBER, rememberMe)
        sharedPreferencesEditor.apply()
    }
}