package app.skypub.data.repository

import app.skypub.network.model.GetProfileResponse
import app.skypub.network.model.GetTimeLineResponse
import app.skypub.network.model.RequestErrorResponse
import arrow.core.Either
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getTimeLine(
        algorithm: String? = null,
        limit: Int? = null,
        cursor: String? = null
    ): Flow<GetTimeLineResponse>

    suspend fun getProfile(): Either<RequestErrorResponse, GetProfileResponse>
}
