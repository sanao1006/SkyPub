package app.skypub.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomNavigationBarMenu(val label: String, val icon: ImageVector) {
    Home("Home", Icons.Filled.Home),
    Notifications("Notifications", Icons.Filled.Notifications),
//    Messages("Messages", Icons.AutoMirrored.Filled.Message)
}
