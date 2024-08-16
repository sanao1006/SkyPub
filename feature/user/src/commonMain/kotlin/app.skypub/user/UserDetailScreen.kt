package app.skypub.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import app.skypub.ui.ScaffoldScreenContent
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.koin.compose.koinInject

class UserDetailScreen(private val handle: String) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewmodel = koinInject<UserDetailScreenViewModel>()
        LaunchedEffect(Unit) {
            viewmodel.getUiState(handle)
        }
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
                when (val state = viewmodel.uiState.collectAsState().value) {
                    is UserDetailScreenState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is UserDetailScreenState.Error -> {
                        Text("error")
                        Text(state.message)
                    }

                    is UserDetailScreenState.UserDetailScreenUiState -> {
                        ScaffoldScreenContent(
                            items = state.feeds,
                            modifier = Modifier.padding(it)
                        ) {
                            Text(
                                it.post.record.jsonObject["text"]?.jsonPrimitive?.content ?: "aa"
                            )
                        }
                    }
                }
            }
        }
    }
}