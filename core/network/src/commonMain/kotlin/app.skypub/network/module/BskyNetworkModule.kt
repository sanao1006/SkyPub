package app.skypub.network.module

import app.skypub.network.Client
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import org.koin.dsl.module

val bskyModule = module {
    val baseUrl = "https://bsky.social/xrpc"
    single {
        val ktorClient: HttpClient = Client().client
        Ktorfit.Builder()
            .httpClient(ktorClient)
            .baseUrl(baseUrl)
            .build()
    }
}