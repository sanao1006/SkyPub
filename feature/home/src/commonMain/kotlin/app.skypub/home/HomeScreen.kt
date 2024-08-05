package app.skypub.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.koin.compose.koinInject

class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val viewmodel: HomeViewModel = koinInject<HomeViewModel>()
        Scaffold {
            Column {
                Text(text = "Home")
                val feeds = viewmodel.feed.collectAsState()
                LazyColumn {
                    items(feeds.value) { feed ->
                        Text(text = feed.post.author.displayName)
                        Text(
                            text = feed.post.record.jsonObject["text"]?.jsonPrimitive?.content ?: ""
                        )
                    }
                }
            }
        }
    }
}