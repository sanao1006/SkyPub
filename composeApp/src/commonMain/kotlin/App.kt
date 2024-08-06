import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import app.skypub.feature.auth.AuthScreenNavigation
import app.skypub.home.HomeScreen
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
    MaterialTheme {
        KoinContext {
            if (isAlreadyLogin) {
                Navigator(HomeScreen())
            } else {
                Navigator(AuthScreenNavigation())
            }
        }
    }
}