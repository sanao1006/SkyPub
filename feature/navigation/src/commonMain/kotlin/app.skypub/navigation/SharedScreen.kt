package app.skypub.navigation

import app.skypub.common.ProfileUiState
import app.skypub.common.ScreenType
import cafe.adriel.voyager.core.registry.ScreenProvider

sealed class SharedScreen : ScreenProvider {
    abstract val profileUiState: ProfileUiState
    abstract val screenType: ScreenType

    data class Home(
        override val profileUiState: ProfileUiState,
        override val screenType: ScreenType
    ) : SharedScreen()

    data class Notification(
        override val profileUiState: ProfileUiState,
        override val screenType: ScreenType
    ) : SharedScreen()
}

sealed class UserScreen : ScreenProvider {
    data class UserDetail(
        val handle: String
    ) : UserScreen()
}

sealed class ReplyScreen : ScreenProvider {
    data class Reply(
        val name: String,
        val handle: String,
        val thumbnail: String,
        val post: String
    ) : ReplyScreen()
}