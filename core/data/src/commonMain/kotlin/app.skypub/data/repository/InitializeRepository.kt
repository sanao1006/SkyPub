package app.skypub.data.repository

import app.skypub.network.model.CreateSessionError
import app.skypub.network.model.CreateSessionResponse
import arrow.core.Either

interface InitializeRepository {
    suspend fun refreshToken(): Either<CreateSessionError, CreateSessionResponse>
    suspend fun initializeToken()
}