package app.skypub.network

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import app.skypub.network.model.CreateRecordInput
import app.skypub.network.model.CreateRecordRequestBody
import app.skypub.network.model.CreateRecordResponse
import app.skypub.network.model.CreateSessionResponse
import app.skypub.network.model.GetAuthorFeedResponse
import app.skypub.network.model.GetListNotificationsResponse
import app.skypub.network.model.GetProfileResponse
import app.skypub.network.model.GetTimeLineResponse
import app.skypub.network.model.RequestErrorResponse
import app.skypub.network.service.BlueskyApi
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable

class BlueskyApiDataSource(
    private val client: HttpClient,
    private val dataStore: DataStore<Preferences>
) : BlueskyApi {
    override suspend fun createSession(
        identifier: String,
        password: String
    ): Either<RequestErrorResponse, CreateSessionResponse> {
        val request = client.post(
            "$BASE_URL/com.atproto.server.createSession"
        ) {
            contentType(ContentType.Application.Json)
            setBody(
                BlueskyAuthInput(
                    identifier = identifier,
                    password = password
                )
            )
        }
        return when (request.status) {
            HttpStatusCode.OK -> {
                request.body<CreateSessionResponse>().right()
            }

            else -> {
                request.body<RequestErrorResponse>().left()
            }
        }
    }

    override fun getTimeLine(
        algorithm: String?,
        limit: Int?,
        cursor: String?
    ): Flow<GetTimeLineResponse> = flow {
        val accessJwt = dataStore.data.first()[stringPreferencesKey("access_jwt")] ?: ""
        val request = client.get("$BASE_URL/app.bsky.feed.getTimeline") {
            contentType(ContentType.Application.Json)
            header(HttpHeaders.Authorization, "Bearer $accessJwt")
        }
        emit(request.body())
    }

    override suspend fun refreshToken(): Either<RequestErrorResponse, CreateSessionResponse> {
        val request = client.post(
            "$BASE_URL/com.atproto.server.refreshSession"
        ) {
            contentType(ContentType.Application.Json)
            val refreshJwt = dataStore.data.first()[stringPreferencesKey("refresh_jwt")] ?: ""
            header(HttpHeaders.Authorization, "Bearer $refreshJwt")
        }
        return when (request.status) {
            HttpStatusCode.OK -> {
                request.body<CreateSessionResponse>().right()
            }

            else -> {
                request.body<RequestErrorResponse>().left()
            }
        }
    }

    override suspend fun createRecord(
        identifier: String,
        collection: String,
        rkey: String?,
        validate: Boolean,
        input: CreateRecordInput
    ): Either<RequestErrorResponse, CreateRecordResponse> {
        val request = client.post(
            "$BASE_URL/com.atproto.repo.createRecord"
        ) {
            contentType(ContentType.Application.Json)
            val accessJwt = dataStore.data.first()[stringPreferencesKey("access_jwt")] ?: ""
            header(HttpHeaders.Authorization, "Bearer $accessJwt")
            setBody(
                CreateRecordRequestBody(
                    repo = identifier,
                    collection = collection,
                    rkey = rkey,
                    validate = validate,
                    record = input
                )
            )
        }
        return when (request.status) {
            HttpStatusCode.OK -> {
                request.body<CreateRecordResponse>().right()
            }

            else -> {
                request.body<RequestErrorResponse>().left()
            }
        }
    }

    override suspend fun getProfile(identifier: String): Either<RequestErrorResponse, GetProfileResponse> {
        val request = client.get(
            "$BASE_URL/app.bsky.actor.getProfile"
        ) {
            contentType(ContentType.Application.Json)
            val accessJwt = dataStore.data.first()[stringPreferencesKey("access_jwt")] ?: ""
            parameter("actor", identifier)
            header(HttpHeaders.Authorization, "Bearer $accessJwt")
        }
        return when (request.status) {
            HttpStatusCode.OK -> {
                request.body<GetProfileResponse>().right()
            }

            else -> {
                request.body<RequestErrorResponse>().left()
            }
        }
    }

    override suspend fun getListNotifications(
        limit: Int?,
        priority: Boolean?,
        cursor: String?,
    ): Either<RequestErrorResponse, GetListNotificationsResponse> {
        val request = client.get(
            "$BASE_URL/app.bsky.notification.listNotifications"
        ) {
            contentType(ContentType.Application.Json)
            val accessJwt = dataStore.data.first()[stringPreferencesKey("access_jwt")] ?: ""
            limit?.let { parameter("limit", it) }
            priority?.let { parameter("priority", it) }
            cursor?.let { parameter("cursor", it) }
            header(HttpHeaders.Authorization, "Bearer $accessJwt")
        }
        return when (request.status) {
            HttpStatusCode.OK -> {
                request.body<GetListNotificationsResponse>().right()
            }

            else -> {
                request.body<RequestErrorResponse>().left()
            }
        }
    }

    override suspend fun getAuthorFeed(handle: String): Either<RequestErrorResponse, GetAuthorFeedResponse> {
        val request = client.get(
            "$BASE_URL/app.bsky.feed.getAuthorFeed"
        ) {
            contentType(ContentType.Application.Json)
            val accessJwt = dataStore.data.first()[stringPreferencesKey("access_jwt")] ?: ""
            parameter("actor", handle)
            header(HttpHeaders.Authorization, "Bearer $accessJwt")
        }
        return when (request.status) {
            HttpStatusCode.OK -> {
                request.body<GetAuthorFeedResponse>().right()
            }

            else -> {
                request.body<RequestErrorResponse>().left()
            }
        }
    }

    override suspend fun deleteSession(): Either<RequestErrorResponse, Unit> {
        val request = client.post(
            "$BASE_URL/com.atproto.server.deleteSession"
        ) {
            contentType(ContentType.Application.Json)
            val accessJwt = dataStore.data.first()[stringPreferencesKey("refresh_jwt")] ?: ""
            header(HttpHeaders.Authorization, "Bearer $accessJwt")
        }
        return when (request.status) {
            HttpStatusCode.OK -> {
                Unit.right()
            }

            else -> {
                request.body<RequestErrorResponse>().left()
            }
        }
    }

    companion object {
        val BASE_URL = "https://bsky.social/xrpc"
    }
}

@Serializable
data class BlueskyAuthInput(
    val identifier: String,
    val password: String
)