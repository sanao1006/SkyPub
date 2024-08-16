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
    abstract val handle: String

    data class UserDetail(
        override val handle: String
    ) : UserScreen()
}