import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import app.skypub.feature.auth.AuthScreenNavigation
import cafe.adriel.voyager.navigator.Navigator
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext

@Composable
@Preview
fun App() {
    Napier.base(DebugAntilog())
    MaterialTheme {
        KoinContext {
            Navigator(AuthScreenNavigation())
        }
    }
}