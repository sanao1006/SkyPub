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
import app.skypub.home.HomeScreen
import app.skypub.network.model.CreateSessionResponse
import app.skypub.network.model.RequestErrorResponse
import arrow.core.Either
import cafe.adriel.voyager.navigator.Navigator
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val dataStore: DataStore<Preferences>,
    private val initializeRepository: InitializeRepository
) : ViewModel() {
    private var _isLoginSuccess: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val isLoginSuccess: StateFlow<Boolean?> = _isLoginSuccess.asStateFlow()

    private val _profileUiState = MutableStateFlow(ProfileUiState("", "", "", 0, 0))
    val profileUiState = _profileUiState.asStateFlow()

    private suspend fun createSession(
        identifier: String,
        password: String
    ): Either<RequestErrorResponse, CreateSessionResponse> {
        return authRepository.createSession(identifier, password)
    }

    fun login(identifier: String, password: String, navigator: Navigator) {
        viewModelScope.launch {
            when (val response = createSession(identifier, password)) {
                is Either.Left -> {
                    _isLoginSuccess.value = false
                    Napier.e(tag = "createSessionError") { "message: ${response.value.message}" }
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
                    _isLoginSuccess.value = true
                    fetchProfile()
                    navigator.popUntilRoot()
                    navigator.replace(HomeScreen(profileUiState.value))
                }
            }
        }
    }

    private fun fetchProfile() {
        viewModelScope.launch {
            when (val result = initializeRepository.getProfile()) {
                is Either.Right -> {
                    _profileUiState.value = ProfileUiState(
                        avatar = result.value.avatar,
                        handle = result.value.handle,
                        displayName = result.value.displayName,
                        followsCount = result.value.followsCount,
                        followersCount = result.value.followersCount
                    )
                }

                is Either.Left -> {
                    Napier.e(tag = "getProfileError") {
                        "message: ${result.value.message}"
                    }
                }
            }
        }
    }
}