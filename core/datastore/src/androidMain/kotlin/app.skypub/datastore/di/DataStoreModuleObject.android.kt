package app.skypub.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import app.skypub.datastore.createDataStore


object DataStoreModule {
    fun provideDataStore(context: Context): DataStore<Preferences> {
        return createDataStore(context)
    }
}