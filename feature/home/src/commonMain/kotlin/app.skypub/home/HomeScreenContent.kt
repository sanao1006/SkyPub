package app.skypub.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import app.skypub.network.model.FeedItem
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ComposableImageRequest
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
            LazyColumn(
                modifier = Modifier.padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                itemsIndexed(feeds) { index, feed ->
                    if (index != 0) {
                        HorizontalDivider()
                    }
                    HomeScreenPostItem(
                        feed = feed,
                        modifier = Modifier
                            .padding(vertical = 12.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun HomeScreenPostItem(
    feed: FeedItem, modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        AsyncImage(
            modifier = Modifier.size(40.dp).clip(CircleShape),
            contentScale = ContentScale.Crop,
            request = ComposableImageRequest(feed.post.author.avatar),
            contentDescription = ""
        )
        Spacer(modifier = Modifier.size(8.dp))
        Column(modifier = Modifier.weight(1f)) {

            Text(text = feed.post.author.displayName)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "@${feed.post.author.handle}",
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = feed.post.record.jsonObject["text"]?.jsonPrimitive?.content ?: "",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
