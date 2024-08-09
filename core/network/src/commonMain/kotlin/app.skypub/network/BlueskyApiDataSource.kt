package app.skypub.network

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import app.skypub.network.model.CreateSessionError
import app.skypub.network.model.CreateSessionResponse
import app.skypub.network.model.GetTimeLineResponse
import app.skypub.network.service.BlueskyApi
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
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
    ): Either<CreateSessionError, CreateSessionResponse> {
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
                request.body<CreateSessionError>().left()
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

    override suspend fun refreshToken(): CreateSessionResponse {
        return client.post(
            "$BASE_URL/com.atproto.server.refreshSession"
        ) {
            contentType(ContentType.Application.Json)
            val refreshJwt = dataStore.data.first()[stringPreferencesKey("refresh_jwt")] ?: ""
            header(HttpHeaders.Authorization, "Bearer $refreshJwt")
        }.body()
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