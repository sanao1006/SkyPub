package app.skypub.datastore.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val dataStoreModule: Module = module {
    single { DataStoreModule.provideDataStore(androidContext()) }
}