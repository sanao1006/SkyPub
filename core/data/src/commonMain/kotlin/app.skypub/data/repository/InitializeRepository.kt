package app.skypub.data.repository

import app.skypub.network.model.CreateSessionResponse
import app.skypub.network.model.RequestErrorResponse
import arrow.core.Either

interface InitializeRepository {
    suspend fun refreshToken(): Either<RequestErrorResponse, CreateSessionResponse>
    suspend fun initializeToken()
}