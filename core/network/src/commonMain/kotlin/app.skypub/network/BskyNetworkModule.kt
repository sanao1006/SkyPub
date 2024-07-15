package app.skypub.network

import de.jensklingenberg.ktorfit.Ktorfit
import org.koin.dsl.module

val bskyModule = module {
    val baseUrl = "https://bsky.social/xrpc"
    single {
        val ktorClient = client
        Ktorfit.Builder()
            .httpClient(ktorClient)
            .baseUrl(baseUrl)
            .build()
    }
}