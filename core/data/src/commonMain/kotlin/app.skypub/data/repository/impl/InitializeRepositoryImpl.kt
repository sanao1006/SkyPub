package app.skypub.data.repository.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import app.skypub.data.repository.InitializeRepository
import app.skypub.network.BlueskyApiDataSource
import app.skypub.network.model.CreateSessionResponse

class InitializeRepositoryImpl(
    private val blueskyApiDataSource: BlueskyApiDataSource,
    private val dataStore: DataStore<Preferences>
) : InitializeRepository {
    override suspend fun refreshToken(): CreateSessionResponse {
        return blueskyApiDataSource.refreshToken()
    }

    override suspend fun initializeToken() {
        val response = refreshToken()
        dataStore.edit {
            val accessJwtKey = stringPreferencesKey("access_jwt")
            val refreshJwtKey = stringPreferencesKey("refresh_jwt")
            it[accessJwtKey] = response.accessJwt
            it[refreshJwtKey] = response.refreshJwt
        }
    }
}
