package app.skypub.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

actual class Client actual constructor() {
    actual val client: HttpClient = HttpClient(Darwin) {
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
