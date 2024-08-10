package app.skypub.data.repository.impl

import app.skypub.data.repository.HomeRepository
import app.skypub.network.BlueskyApiDataSource
import app.skypub.network.model.GetProfileResponse
import app.skypub.network.model.GetTimeLineResponse
import app.skypub.network.model.RequestErrorResponse
import arrow.core.Either
import kotlinx.coroutines.flow.Flow

class HomeRepositoryImpl(
    private val blueskyApiDataSource: BlueskyApiDataSource
) : HomeRepository {
    override fun getTimeLine(
        algorithm: String?,
        limit: Int?,
        cursor: String?
    ): Flow<GetTimeLineResponse> {
        return blueskyApiDataSource.getTimeLine(algorithm, limit, cursor)
    }

    override suspend fun getProfile(): Either<RequestErrorResponse, GetProfileResponse> {
        return blueskyApiDataSource.getProfile()
    }
}