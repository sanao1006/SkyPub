package app.skypub.data.repository

import app.skypub.network.model.GetAuthorFeedResponse
import app.skypub.network.model.GetProfileResponse
import app.skypub.network.model.RequestErrorResponse
import arrow.core.Either

interface UserRepository {
    suspend fun getAuthorFeed(handle: String): Either<RequestErrorResponse, GetAuthorFeedResponse>
    suspend fun getProfile(identifier: String): Either<RequestErrorResponse, GetProfileResponse>
    suspend fun deleteSession(): Either<RequestErrorResponse, Unit>
}