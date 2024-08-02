package app.skypub.network

import app.skypub.network.model.CreateSessionResponse
import app.skypub.network.service.BlueskyApi
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class BlueskyApiDataSource(
    private val client: HttpClient
) : BlueskyApi {
    override suspend fun createSession(
        identifier: String,
        password: String
    ): CreateSessionResponse {
        return client.post(
            "$BASE_URL/com.atproto.server.createSession"
        ) {
            setBody(
                """
                {
                    "identifier": "$identifier",
                    "password": "$password"
                }
                """.trimIndent()
            )
        }.body()
    }

    companion object {
        val BASE_URL = "https://bsky.social/xrpc"
    }
}