package app.skypub.data.repository.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import app.skypub.data.repository.InitializeRepository
import app.skypub.network.BlueskyApiDataSource
import app.skypub.network.model.CreateSessionResponse
import app.skypub.network.model.RequestErrorResponse
import arrow.core.Either
import io.github.aakira.napier.Napier

class InitializeRepositoryImpl(
    private val blueskyApiDataSource: BlueskyApiDataSource,
    private val dataStore: DataStore<Preferences>
) : InitializeRepository {
    override suspend fun refreshToken(): Either<RequestErrorResponse, CreateSessionResponse> {
        return blueskyApiDataSource.refreshToken()
    }

    override suspend fun initializeToken() {
        when (val response = refreshToken()) {
            is Either.Right -> {
                dataStore.edit {
                    val accessJwtKey = stringPreferencesKey("access_jwt")
                    val refreshJwtKey = stringPreferencesKey("refresh_jwt")
                    it[accessJwtKey] = response.value.accessJwt
                    it[refreshJwtKey] = response.value.refreshJwt
                }
            }

            is Either.Left -> {
                dataStore.edit {
                    val accessJwtKey = stringPreferencesKey("access_jwt")
                    val refreshJwtKey = stringPreferencesKey("refresh_jwt")
                    it.remove(accessJwtKey)
                    it.remove(refreshJwtKey)
                }
                Napier.e(tag = "refreshTokenError") { "message: ${response.value.message}" }
            }
        }
    }
}
