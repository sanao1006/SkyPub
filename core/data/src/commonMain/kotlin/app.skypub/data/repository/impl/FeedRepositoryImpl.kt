package app.skypub.data.repository.impl

import app.skypub.data.repository.FeedRepository
import app.skypub.network.BlueskyApiDataSource
import app.skypub.network.model.GetTimeLineResponse
import kotlinx.coroutines.flow.Flow

class FeedRepositoryImpl(
    private val blueskyApiDataSource: BlueskyApiDataSource
) : FeedRepository {
    override fun getTimeLine(
        algorithm: String?,
        limit: Int?,
        cursor: String?
    ): Flow<GetTimeLineResponse> {
        return blueskyApiDataSource.getTimeLine(algorithm, limit, cursor)
    }
}