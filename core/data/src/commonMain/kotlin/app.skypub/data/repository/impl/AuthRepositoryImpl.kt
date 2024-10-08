package app.skypub.data.repository.impl

import app.skypub.data.repository.AuthRepository
import app.skypub.network.BlueskyApiDataSource
import app.skypub.network.model.CreateSessionResponse
import app.skypub.network.model.RequestErrorResponse
import arrow.core.Either

class AuthRepositoryImpl(
    private val blueskyApi: BlueskyApiDataSource
) : AuthRepository {

    override suspend fun createSession(
        identifier: String,
        password: String
    ): Either<RequestErrorResponse, CreateSessionResponse> {
        return blueskyApi.createSession(identifier, password)
    }
}