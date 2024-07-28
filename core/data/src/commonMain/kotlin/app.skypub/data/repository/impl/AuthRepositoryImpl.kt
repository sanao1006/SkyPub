package app.skypub.data.repository.impl

import app.skypub.data.repository.AuthRepository
import app.skypub.network.model.CreateSessionResponse
import app.skypub.network.service.BlueskyService
import io.github.aakira.napier.Napier

class AuthRepositoryImpl(
    private val bskyService: BlueskyService
) : AuthRepository {

    override suspend fun createSession(
        identifier: String,
        password: String
    ): CreateSessionResponse {
        return bskyService.createSession(identifier, password)
    }

    override fun hello() {
        Napier.d("Hello from AuthRepositoryImpl", tag = "ray")
    }
}