package app.skypub.common

data class ProfileUiState(
    var avatar: String = "",
    var handle: String = "",
    var displayName: String = "",
    var followsCount: Int = 0,
    var followersCount: Int = 0,
)