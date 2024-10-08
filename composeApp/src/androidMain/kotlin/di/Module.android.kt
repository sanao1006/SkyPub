package di

import io.ktor.client.engine.android.Android
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single { Android.create() }
}