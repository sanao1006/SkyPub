package app.skypub.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import org.koin.compose.koinInject

class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val viewmodel: HomeViewModel = koinInject<HomeViewModel>()
        Scaffold {
            Column {
                val feeds = viewmodel.feed.value
                LazyColumn {
                    items(feeds) { feed ->
                        Text(text = feed.post.author.displayName)
                    }
                }
            }
        }
    }
}