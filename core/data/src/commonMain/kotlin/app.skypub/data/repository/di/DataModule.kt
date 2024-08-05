package app.skypub.data.repository.di

import app.skypub.data.repository.AuthRepository
import app.skypub.data.repository.FeedRepository
import app.skypub.data.repository.InitializeRepository
import app.skypub.data.repository.impl.AuthRepositoryImpl
import app.skypub.data.repository.impl.FeedRepositoryImpl
import app.skypub.data.repository.impl.InitializeRepositoryImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()
    singleOf(::FeedRepositoryImpl).bind<FeedRepository>()
    singleOf(::InitializeRepositoryImpl).bind<InitializeRepository>()
}