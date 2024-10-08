package app.skypub.network.service

import app.skypub.network.model.CreateRecordInput
import app.skypub.network.model.CreateRecordResponse
import app.skypub.network.model.CreateSessionResponse
import app.skypub.network.model.GetAuthorFeedResponse
import app.skypub.network.model.GetListNotificationsResponse
import app.skypub.network.model.GetProfileResponse
import app.skypub.network.model.GetTimeLineResponse
import app.skypub.network.model.RequestErrorResponse
import arrow.core.Either
import kotlinx.coroutines.flow.Flow

interface BlueskyApi {
    suspend fun createSession(
        identifier: String,
        password: String
    ): Either<RequestErrorResponse, CreateSessionResponse>

    fun getTimeLine(
        algorithm: String? = null,
        limit: Int? = null,
        cursor: String? = null
    ): Flow<GetTimeLineResponse>

    suspend fun refreshToken(): Either<RequestErrorResponse, CreateSessionResponse>

    suspend fun createRecord(
        identifier: String,
        collection: String,
        rkey: String? = null,
        validate: Boolean = true,
        input: CreateRecordInput
    ): Either<RequestErrorResponse, CreateRecordResponse>

    suspend fun getProfile(identifier: String): Either<RequestErrorResponse, GetProfileResponse>

    suspend fun getListNotifications(
        limit: Int?,
        priority: Boolean?,
        cursor: String?,
    ): Either<RequestErrorResponse, GetListNotificationsResponse>

    suspend fun getAuthorFeed(
        handle: String,
//        limit: Int?,
//        cursor: String?,
    ): Either<RequestErrorResponse, GetAuthorFeedResponse>

    suspend fun deleteSession(): Either<RequestErrorResponse, Unit>
}
