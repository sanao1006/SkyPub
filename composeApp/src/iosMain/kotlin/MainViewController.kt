import androidx.compose.ui.window.ComposeUIViewController
import di.bskyModule
import org.koin.core.context.startKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        startKoin { modules(bskyModule) }
    }
) { App() }