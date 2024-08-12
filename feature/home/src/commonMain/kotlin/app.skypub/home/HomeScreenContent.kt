package app.skypub.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.skypub.network.model.FeedItem
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Composable
fun HomeScreenContent(
    feeds: List<FeedItem>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (feeds.isEmpty()) {
            CircularProgressIndicator()
        } else {
            LazyColumn {
                items(feeds) { feed ->
                    HomeScreenPostItem(feed = feed)
                }
            }
        }
    }
}

@Composable
fun HomeScreenPostItem(
    feed: FeedItem,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(text = feed.post.author.displayName)
        Text(
            text = feed.post.record.jsonObject["text"]?.jsonPrimitive?.content
                ?: ""
        )
    }
}
