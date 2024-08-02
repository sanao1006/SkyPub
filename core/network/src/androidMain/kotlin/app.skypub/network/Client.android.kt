package app.skypub.network

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json

actual class Client actual constructor() {
    actual val client = HttpClient(OkHttp) {
        install(HttpTimeout) {
            socketTimeoutMillis = 60_000
            requestTimeoutMillis = 60_000
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.d("response", message)
                }
            }
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(json = kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
                isLenient = false
                allowSpecialFloatingPointValues = true
                useArrayPolymorphism = false
            })
        }
    }
}