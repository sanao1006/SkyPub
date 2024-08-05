package app.skypub.data.repository

import app.skypub.network.model.CreateSessionResponse

interface InitializeRepository {
    suspend fun refreshToken(): CreateSessionResponse
    suspend fun initializeToken()
}