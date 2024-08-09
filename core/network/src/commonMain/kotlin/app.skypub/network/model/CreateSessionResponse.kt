package app.skypub.network.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateSessionResponse(
    val accessJwt: String,
    val refreshJwt: String,
    val handle: String,
    val did: String,
)

@Serializable
data class CreateSessionError(
    val error: String?,
    val message: String,
)