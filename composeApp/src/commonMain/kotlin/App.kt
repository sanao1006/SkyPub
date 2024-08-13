import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.skypub.common.ScreenType
import app.skypub.feature.auth.AuthScreenNavigation
import app.skypub.home.HomeScreen
import app.skypub.navigation.SharedScreen
import app.skypub.notification.NotificationScreen
import app.skypub.ui.theme.AppTheme
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.navigator.Navigator
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.compose.KoinContext
import org.koin.compose.koinInject

@Composable
fun App() {
    Napier.base(DebugAntilog())
    val viewmodel = koinInject<AppViewModel>()
    val isAlreadyLogin = viewmodel.isAlreadyLogin.collectAsState().value
    val profileUiState = viewmodel.profileUiState.collectAsState().value

    ScreenRegistry {
        register<SharedScreen.Home> {
            HomeScreen(profileUiState, ScreenType.HOME)
        }
        register<SharedScreen.Notification> {
            NotificationScreen(profileUiState, ScreenType.NOTIFICATION)
        }
    }

    AppTheme {
        KoinContext {
            when (isAlreadyLogin) {
                null -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator()
                    }
                }

                true -> {
                    if (profileUiState.handle.isNotBlank()) {
                        Navigator(HomeScreen(profileUiState, ScreenType.HOME))
                    }
                }

                false -> Navigator(AuthScreenNavigation())
            }
        }
    }
}