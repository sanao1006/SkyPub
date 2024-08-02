package app.skypub.network.service

import app.skypub.network.model.CreateSessionResponse

interface BlueskyApi {
    suspend fun createSession(
        identifier: String,
        password: String
    ): CreateSessionResponse
}
