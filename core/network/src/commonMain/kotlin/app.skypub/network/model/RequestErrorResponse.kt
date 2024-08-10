package app.skypub.network.model

import kotlinx.serialization.Serializable

@Serializable
data class RequestErrorResponse(
    val error: String?,
    val message: String,
)