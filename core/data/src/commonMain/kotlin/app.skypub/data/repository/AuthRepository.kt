package app.skypub.data.repository

import app.skypub.network.model.CreateSessionResponse
import app.skypub.network.model.RequestErrorResponse
import arrow.core.Either

interface AuthRepository {
    suspend fun createSession(
        identifier: String,
        password: String
    ): Either<RequestErrorResponse, CreateSessionResponse>
}