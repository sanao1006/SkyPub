package app.skypub.network.service

import app.skypub.network.model.CreateSessionResponse
import app.skypub.network.model.GetTimeLineResponse
import kotlinx.coroutines.flow.Flow

interface BlueskyApi {
    suspend fun createSession(
        identifier: String,
        password: String
    ): CreateSessionResponse

    fun getTimeLine(
        algorithm: String? = null,
        limit: Int? = null,
        cursor: String? = null
    ): Flow<GetTimeLineResponse>

    suspend fun refreshToken(): CreateSessionResponse
}
