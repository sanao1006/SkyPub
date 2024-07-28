package app.skypub.network.service

import app.skypub.network.model.CreateSessionResponse
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST

interface BlueskyService {
    @POST("com.atproto.server.createSession")
    suspend fun createSession(
        @Body identifier: String,
        @Body password: String
    ): CreateSessionResponse
}
