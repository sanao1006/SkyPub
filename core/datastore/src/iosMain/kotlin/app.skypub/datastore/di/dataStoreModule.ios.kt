package app.skypub.datastore.di

import org.koin.dsl.module

actual val dataStoreModule = module {
    single { DataStoreModule.provideDataStore() }
}
