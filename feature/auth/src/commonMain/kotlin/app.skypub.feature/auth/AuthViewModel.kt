package app.skypub.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.skypub.network.service.BlueskyApi
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: BlueskyApi) : ViewModel() {
    suspend fun createSession(identifier: String, password: String) {
        authRepository.createSession(identifier, password)
    }

    fun aa(a: String, b: String) {
        viewModelScope.launch {
            createSession(a, b)
        }
    }
}