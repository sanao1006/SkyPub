package app.skypub.post

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.compose.koinInject

class PostScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
    @Composable
    override fun Content() {
        var text = rememberSaveable { mutableStateOf("") }
        val focusRequester = remember { FocusRequester() }
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: PostViewModel = koinInject<PostViewModel>()
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "")
                        }
                    },
                    actions = {
                        Button(onClick = {
                            viewModel.createPost(text = text.value)
                            navigator.pop()
                        }) {
                            Text("Post")
                        }
                    }
                )
            }
        ) {
            Column(modifier = Modifier.padding(it)) {
                TextField(
                    modifier = Modifier.fillMaxSize().focusRequester(focusRequester),
                    value = text.value, onValueChange = { text.value = it },
                    placeholder = { Text("What's up?") }
                )
            }
        }
    }
}