package app.skypub.project

import android.app.Application
import di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent

class SkyPubApplication : Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@SkyPubApplication)
        }
    }
}