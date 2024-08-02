package di

import app.skypub.data.repository.di.dataModule
import app.skypub.feature.auth.AuthViewModel
import app.skypub.network.module.blueskyModule
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(config: KoinAppDeclaration = {}) {
    startKoin {
        config()
        modules(
            blueskyModule,
            platformModule,
            dataRepositoryModule
        )
    }
}


val dataRepositoryModule = module {
    includes(dataModule)
    viewModelOf(::AuthViewModel)
}
