package di

import app.skypub.data.repository.AuthRepository
import app.skypub.data.repository.impl.AuthRepositoryImpl
import app.skypub.feature.auth.AuthViewModel
import app.skypub.network.module.bskyModule
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(allModules, platformModule)
    }
}

private val allModules = module {
    networkModule()
}

private fun networkModule() = module {
    single { bskyModule }
    module {
        bskyRepositoryModule()
    }
}

private fun bskyRepositoryModule() = module {
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()
    viewModelOf(::AuthViewModel)
}
