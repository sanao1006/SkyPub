package app.skypub.post

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ComposableImageRequest

class ReplyScreen(
    private val name: String,
    private val handle: String,
    private val thumbnail: String,
    private val post: String
) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(Icons.Default.Close, contentDescription = "")
                        }
                    },
                    actions = {
                        IconButton(onClick = {}) {
                            Text("Post")
                        }
                    }
                )
            }
        ) {
            Column(modifier = Modifier.padding(it)) {
                ReplyPost(name = name, thumbnail = thumbnail, post = post)

            }
        }
    }
}

@Composable
fun ReplyPost(
    name: String,
    thumbnail: String,
    post: String
) {
    Row {
        AsyncImage(
            request = ComposableImageRequest(thumbnail),
            contentDescription = "",
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.size(8.dp))
        Column {
            Text(text = name)
            Spacer(modifier = Modifier.size(4.dp))
            Text(text = post)
        }
        Spacer(modifier = Modifier.weight(1f))
    }

}