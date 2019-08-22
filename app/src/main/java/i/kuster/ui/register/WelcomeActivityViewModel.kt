package i.kuster.ui.register

import androidx.lifecycle.ViewModel

class WelcomeActivityViewModel : ViewModel() {
    fun getPrefix(email: String):String {
        return email.substringBefore("@")
    }

}