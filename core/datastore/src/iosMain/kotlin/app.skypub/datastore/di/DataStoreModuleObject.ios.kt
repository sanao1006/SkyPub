package app.skypub.datastore.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import app.skypub.datastore.createDataStore

object DataStoreModule {
    fun provideDataStore(): DataStore<Preferences> =
        createDataStore()
}