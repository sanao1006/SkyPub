package app.skypub.feature.auth

import androidx.lifecycle.ViewModel
import app.skypub.data.repository.AuthRepository

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    fun hello() {
        authRepository.hello()
    }

}