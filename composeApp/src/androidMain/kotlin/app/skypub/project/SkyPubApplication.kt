package app.skypub.project

import android.app.Application
import di.initKoin

class SkyPubApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}