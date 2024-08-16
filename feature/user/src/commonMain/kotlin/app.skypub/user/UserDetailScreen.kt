package app.skypub.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

class UserDetailScreen(val handle: String) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        Scaffold(
            topBar = {
                Row(modifier = Modifier.background(color = Color.Transparent)) {
                    IconButton(onClick = { navigator.pop() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, "")
                    }
                }
            }
        ) {
            Column(modifier = Modifier.padding(it)) {
                Text("User Detail Screen")
            }
        }
    }
}