package app.skypub.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateSessionResponse(
    @SerialName("access_jwt")
    val accessJwt: String,
    @SerialName("refresh_jwt")
    val refreshJwt: String,
)