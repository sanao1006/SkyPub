package app.skypub.post

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen

class PostScreen : Screen {
    @Composable
    override fun Content() {
        var text = rememberSaveable { mutableStateOf("") }
        Scaffold(
            topBar = {}
        ) {
            TextField(
                modifier = Modifier.fillMaxSize(),
                value = text.value, onValueChange = { text.value = it })
        }
    }
}