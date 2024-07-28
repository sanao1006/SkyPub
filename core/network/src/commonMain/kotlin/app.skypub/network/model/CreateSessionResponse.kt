package app.skypub.network.model

data class CreateSessionResponse(
    val accessJwt: String,
    val refreshJwt: String,
    val handle: String,
    val did: String
)