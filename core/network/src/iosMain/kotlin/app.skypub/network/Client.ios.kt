package app.skypub.network

import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json

actual class Client actual constructor() {
    actual val client: HttpClient = HttpClient(Darwin) {
        install(ContentNegotiation) {
            json(
                json = kotlinx.serialization.json.Json {
                    ignoreUnknownKeys = true
                    isLenient = false
                    allowSpecialFloatingPointValues = true
                    useArrayPolymorphism = false
                },
                contentType = ContentType.Application.Json
            )
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Napier.d(tag = "response") { message }
                }
            }
            level = LogLevel.ALL
        }
    }
}
