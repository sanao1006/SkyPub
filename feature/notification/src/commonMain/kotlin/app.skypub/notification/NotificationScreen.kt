package app.skypub.notification

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import cafe.adriel.voyager.core.screen.Screen
import org.koin.compose.koinInject

class NotificationScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinInject<NotificationViewModel>()
        val uiState = viewModel.uiState.collectAsState().value
        LazyColumn {
            items(uiState.notifications) {
                Text(it.name)
            }
        }
    }
}