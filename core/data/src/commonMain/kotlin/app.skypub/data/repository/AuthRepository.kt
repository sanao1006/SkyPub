package app.skypub.data.repository

import app.skypub.network.model.CreateSessionResponse

interface AuthRepository {
    suspend fun createSession(
        identifier: String,
        password: String
    ): CreateSessionResponse
}