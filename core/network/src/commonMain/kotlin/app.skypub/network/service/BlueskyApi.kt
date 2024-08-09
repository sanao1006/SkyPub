package app.skypub.network.service

import app.skypub.network.model.CreateSessionError
import app.skypub.network.model.CreateSessionResponse
import app.skypub.network.model.GetTimeLineResponse
import arrow.core.Either
import kotlinx.coroutines.flow.Flow

interface BlueskyApi {
    suspend fun createSession(
        identifier: String,
        password: String
    ): Either<CreateSessionError, CreateSessionResponse>

    fun getTimeLine(
        algorithm: String? = null,
        limit: Int? = null,
        cursor: String? = null
    ): Flow<GetTimeLineResponse>

    suspend fun refreshToken(): CreateSessionResponse
}
