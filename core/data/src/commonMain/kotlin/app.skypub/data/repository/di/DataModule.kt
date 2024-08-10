package app.skypub.data.repository.di

import app.skypub.data.repository.AuthRepository
import app.skypub.data.repository.HomeRepository
import app.skypub.data.repository.InitializeRepository
import app.skypub.data.repository.PostRepository
import app.skypub.data.repository.impl.AuthRepositoryImpl
import app.skypub.data.repository.impl.HomeRepositoryImpl
import app.skypub.data.repository.impl.InitializeRepositoryImpl
import app.skypub.data.repository.impl.PostRepositoryImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()
    singleOf(::HomeRepositoryImpl).bind<HomeRepository>()
    singleOf(::InitializeRepositoryImpl).bind<InitializeRepository>()
    singleOf(::PostRepositoryImpl).bind<PostRepository>()
}