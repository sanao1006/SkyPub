package app.skypub.common

data class ProfileUiState(
    val avatar: String = "",
    val handle: String = "",
    val displayName: String = "",
    val followsCount: Int = 0,
    val followersCount: Int = 0,
)