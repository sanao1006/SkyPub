package app.skypub.post

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.skypub.network.model.ReplyRef
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ComposableImageRequest
import org.koin.compose.koinInject

class ReplyScreen(
    private val name: String,
    private val handle: String,
    private val thumbnail: String,
    private val post: String,
    private val reply: ReplyRef
) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var isDialogShow by rememberSaveable { mutableStateOf(false) }
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        val text = rememberSaveable { mutableStateOf("") }
        val focusRequester = remember { FocusRequester() }
        val viewModel = koinInject<PostViewModel>()

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        if (isDialogShow) {
            AlertDialog(
                onDismissRequest = { isDialogShow = false },
                title = { Text("Save Draft") },
                text = { Text("Do you want to save this post as a draft?") },
                confirmButton = {
                    TextButton(onClick = {
                        isDialogShow = false
                        navigator.pop()
                    }) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        isDialogShow = false
                        navigator.pop()
                    }) {
                        Text("Discard")
                    }
                }
            )
        }
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = {
                            if (text.value.isBlank()) {
                                navigator.pop()
                            } else {
                                isDialogShow = true
                            }
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "")
                        }
                    },
                    actions = {
                        TextButton(onClick = {
                            viewModel.createReply(
                                text = text.value,
                                ref = reply,
                                onSuccess = {
                                    navigator.pop()
                                },
                                onError = {
                                    navigator.pop()
                                }
                            )
                        }
                        ) {
                            Text("Reply")
                        }
                    }
                )
            },
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ) {
            Column(modifier = Modifier.padding(it)) {
                ReplyPost(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
                    name = name,
                    thumbnail = thumbnail,
                    post = post
                )
                Spacer(modifier = Modifier.size(4.dp))
                HorizontalDivider(
                    modifier = Modifier.align(Alignment.CenterHorizontally).fillMaxWidth(0.95f)
                )
                Spacer(modifier = Modifier.size(4.dp))
                TextField(
                    modifier = Modifier.fillMaxSize().focusRequester(focusRequester),
                    value = text.value, onValueChange = { text.value = it },
                    placeholder = { Text("Reply") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                    )
                )
            }
        }
    }
}

@Composable
fun ReplyPost(
    modifier: Modifier = Modifier,
    name: String,
    thumbnail: String,
    post: String,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top
    ) {
        AsyncImage(
            request = ComposableImageRequest(thumbnail),
            contentDescription = "",
            modifier = Modifier.clip(CircleShape).size(40.dp),
            contentScale = androidx.compose.ui.layout.ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.size(2.dp))
            Text(
                text = post,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.weight(1f))
    }

}