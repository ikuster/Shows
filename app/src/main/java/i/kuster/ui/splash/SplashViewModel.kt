package i.kuster.ui.splash

import android.content.Context
import androidx.lifecycle.ViewModel
import i.kuster.ShowsApp
import i.kuster.data.repository.KEY_REMEMBER
import i.kuster.data.repository.PREF_NAME
import i.kuster.data.repository.sharedPreferences

class SplashViewModel : ViewModel() {
    fun getRememberMe(): Boolean {
        sharedPreferences= ShowsApp.instance.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(KEY_REMEMBER, false)
    }
}