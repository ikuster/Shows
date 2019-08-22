package i.kuster

import android.app.Application

class ShowsApp:Application() {
    companion object {
        lateinit var instance: ShowsApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}