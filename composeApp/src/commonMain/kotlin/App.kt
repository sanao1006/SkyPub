import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.skypub.common.ScreenType
import app.skypub.feature.auth.LoginScreen
import app.skypub.feature.auth.Platform
import app.skypub.home.HomeScreen
import app.skypub.navigation.PostDetailScreen
import app.skypub.navigation.ReplyScreen
import app.skypub.navigation.SharedScreen
import app.skypub.navigation.UserScreen
import app.skypub.notification.NotificationScreen
import app.skypub.ui.theme.AppTheme
import app.skypub.user.UserDetailScreen
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
            HomeScreen(it.profileUiState, ScreenType.HOME)
        }
        register<SharedScreen.Notification> {
            NotificationScreen(it.profileUiState, ScreenType.NOTIFICATION)
        }
        register<UserScreen.UserDetail> {
            UserDetailScreen(it.handle)
        }
        register<app.skypub.ui.LoginScreen.Login> {
            LoginScreen(platform = Platform.Bluesky)
        }
        register<ReplyScreen.Reply> {
            app.skypub.post.ReplyScreen(it.name, it.handle, it.thumbnail, it.post, it.ref)
        }
        register<PostDetailScreen.PostDetail> {
            com.example.postdetail.PostDetailScreen(it.post, it.reply)
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

                false -> Navigator(
                    LoginScreen(platform = Platform.Bluesky)
                )
            }
        }
    }
}