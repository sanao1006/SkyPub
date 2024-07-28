package app.skypub.network

import io.ktor.client.HttpClient

expect class Client() {
    val client: HttpClient
}
