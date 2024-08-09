package app.skypub.data.repository

import app.skypub.network.model.CreateSessionError
import app.skypub.network.model.CreateSessionResponse
import arrow.core.Either

interface AuthRepository {
    suspend fun createSession(
        identifier: String,
        password: String
    ): Either<CreateSessionError, CreateSessionResponse>
}