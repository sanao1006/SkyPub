package app.skypub.data.repository.impl

import app.skypub.data.repository.UserRepository
import app.skypub.network.BlueskyApiDataSource
import app.skypub.network.model.GetAuthorFeedResponse
import app.skypub.network.model.GetProfileResponse
import app.skypub.network.model.RequestErrorResponse
import arrow.core.Either

class UserRepositoryImpl(
    private val blueskyApiDataSource: BlueskyApiDataSource
) : UserRepository {
    override suspend fun getAuthorFeed(handle: String): Either<RequestErrorResponse, GetAuthorFeedResponse> {
        return blueskyApiDataSource.getAuthorFeed(handle)
    }

    override suspend fun getProfile(identifier: String): Either<RequestErrorResponse, GetProfileResponse> {
        return blueskyApiDataSource.getProfile(identifier)
    }

    override suspend fun deleteSession(): Either<RequestErrorResponse, Unit> {
        return blueskyApiDataSource.deleteSession()
    }
}