package di

import app.skypub.data.repository.AuthRepository
import app.skypub.data.repository.impl.AuthRepositoryImpl
import app.skypub.feature.auth.AuthViewModel
import app.skypub.network.module.bskyModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(networkModule())
    }

private fun networkModule() = module {
    single { bskyModule }
    module {
        bskyRepositoryModule()
    }
}

private fun bskyRepositoryModule() = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    factory { AuthViewModel(get()) }
}
