package app.skypub.navigation

import app.skypub.common.ProfileUiState
import cafe.adriel.voyager.core.registry.ScreenProvider

sealed class SharedScreen : ScreenProvider {
    abstract val profileUiState: ProfileUiState

    data class Home(override val profileUiState: ProfileUiState) : SharedScreen()
    data class Notification(override val profileUiState: ProfileUiState) : SharedScreen()
}
