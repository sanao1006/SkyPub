package di

import AppViewModel
import app.skypub.data.repository.di.dataModule
import app.skypub.datastore.di.dataStoreModule
import app.skypub.feature.auth.AuthViewModel
import app.skypub.home.HomeViewModel
import app.skypub.network.module.blueskyModule
import app.skypub.notification.NotificationViewModel
import app.skypub.post.PostViewModel
import app.skypub.ui.NavDrawerViewModel
import app.skypub.user.UserDetailScreenViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(config: KoinAppDeclaration = {}) = startKoin {
    config()
    modules(
        blueskyModule,
        platformModule,
        dataRepositoryModule,
        dataStoreModule
    )
}


val dataRepositoryModule = module {
    includes(dataModule)
    viewModelOf(::AuthViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::AppViewModel)
    viewModelOf(::PostViewModel)
    viewModelOf(::NotificationViewModel)
    viewModelOf(::UserDetailScreenViewModel)
    viewModelOf(::NavDrawerViewModel)
}
