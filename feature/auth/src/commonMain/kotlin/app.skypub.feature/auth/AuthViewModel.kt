package app.skypub.feature.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.skypub.common.ProfileUiState
import app.skypub.data.repository.AuthRepository
import app.skypub.data.repository.InitializeRepository
import app.skypub.network.model.CreateSessionResponse
import app.skypub.network.model.RequestErrorResponse
import arrow.core.Either
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val dataStore: DataStore<Preferences>,
    private val initializeRepository: InitializeRepository
) : ViewModel() {
    private var _profileUiState = MutableStateFlow(ProfileUiState("", "", "", 0, 0))
    val profileUiState = _profileUiState.asStateFlow()

    private suspend fun createSession(
        identifier: String,
        password: String
    ): Either<RequestErrorResponse, CreateSessionResponse> {
        return authRepository.createSession(identifier, password)
    }

    fun login(
        identifier: String,
        password: String,
        onSuccess: suspend () -> Unit = {},
        onFailure: suspend () -> Unit = {}
    ) {
        viewModelScope.launch {
            when (val response = createSession(identifier, password)) {
                is Either.Left -> {
                    Napier.e(tag = "createSessionError") { "message: ${response.value.message}" }
                    onFailure()
                }

                is Either.Right -> {
                    dataStore.edit {
                        val accessJwtKey = stringPreferencesKey("access_jwt")
                        val refreshJwtKey = stringPreferencesKey("refresh_jwt")
                        val identifierKey = stringPreferencesKey("identifier")
                        it[accessJwtKey] = response.value.accessJwt
                        it[refreshJwtKey] = response.value.refreshJwt
                        it[identifierKey] = identifier
                    }
                    fetchProfile()
                    onSuccess()
                }
            }
        }
    }

    private suspend fun fetchProfile() {
        val identifier = dataStore.data.first()[stringPreferencesKey("identifier")] ?: ""
        when (val result = initializeRepository.getProfile(identifier)) {
            is Either.Right -> {
                _profileUiState.value.avatar = result.value.avatar
                _profileUiState.value.handle = result.value.handle
                _profileUiState.value.displayName = result.value.displayName
                _profileUiState.value.followsCount = result.value.followsCount
                _profileUiState.value.followersCount = result.value.followersCount
            }

            is Either.Left -> {
                Napier.e(tag = "getProfileError") {
                    "message: ${result.value.message}"
                }
            }
        }
    }
}