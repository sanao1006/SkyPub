package app.skypub.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.skypub.network.service.BlueskyApi
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: BlueskyApi) : ViewModel() {
    suspend fun createSession(identifier: String, password: String) {
        authRepository.createSession(identifier, password)
    }

    fun login(identifier: String, password: String) {
        viewModelScope.launch {
            try {
                createSession(identifier, password)
            } catch (e: Exception) {
                Napier.e(tag = "loginError") { "message: ${e.message}" }
            }
        }
    }
}