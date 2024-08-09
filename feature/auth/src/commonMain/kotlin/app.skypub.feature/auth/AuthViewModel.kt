package app.skypub.feature.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.skypub.data.repository.AuthRepository
import app.skypub.home.HomeScreen
import app.skypub.network.model.CreateSessionError
import app.skypub.network.model.CreateSessionResponse
import arrow.core.Either
import cafe.adriel.voyager.navigator.Navigator
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {
    private var _isLoginSuccess: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val isLoginSuccess: StateFlow<Boolean?> = _isLoginSuccess.asStateFlow()

    private suspend fun createSession(
        identifier: String,
        password: String
    ): Either<CreateSessionError, CreateSessionResponse> {
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
                    navigator.popUntilRoot()
                    navigator.replace(HomeScreen())
                }
            }
        }
    }
}