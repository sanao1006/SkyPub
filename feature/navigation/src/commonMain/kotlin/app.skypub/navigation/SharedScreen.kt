package app.skypub.navigation

import cafe.adriel.voyager.core.registry.ScreenProvider

sealed class SharedScreen : ScreenProvider {
    data object Home : SharedScreen()
    data object Notification : SharedScreen()
}