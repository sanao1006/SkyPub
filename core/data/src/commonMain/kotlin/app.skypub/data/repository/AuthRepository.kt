package app.skypub.data.repository

import org.koin.core.component.KoinComponent

interface AuthRepository : KoinComponent {
    //    suspend fun createSession(identifier: String, password: String): CreateSessionResponse
    fun hello()
}
