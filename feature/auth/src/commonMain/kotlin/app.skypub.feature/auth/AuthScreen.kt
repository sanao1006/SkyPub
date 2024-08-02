package app.skypub.feature.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.compose.koinInject

class AuthScreenNavigation : Screen {
    @Composable
    override fun Content() {
        val viewModel: AuthViewModel = koinInject<AuthViewModel>()
        val navigator = LocalNavigator.currentOrThrow
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Hello! choose platform!",
                style = MaterialTheme.typography.headlineMedium,

                )
            Spacer(modifier = Modifier.height(24.dp))
            TextButton(
                onClick = { navigator.push(LoginScreen(platform = Platform.Bluesky)) }
            ) {
                Text(
                    text = Platform.Bluesky.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = { navigator.push(LoginScreen(Platform.Misskey)) }) {
                Text(
                    text = Platform.Misskey.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

enum class Platform(val value: String) {
    Bluesky("Bluesky"),
    Misskey("Misskey")
}
