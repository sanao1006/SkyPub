package app.skypub.network.module

import app.skypub.network.BlueskyApiDataSource
import app.skypub.network.Client
import app.skypub.network.service.BlueskyApi
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val blueskyModule = module {
    single { Client().client }
    singleOf(::BlueskyApiDataSource).bind<BlueskyApi>()
}