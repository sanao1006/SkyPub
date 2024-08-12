package app.skypub.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.sharp.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
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
                            .padding(top = 12.dp)
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
            Text(
                text = feed.post.author.displayName,
                style = MaterialTheme.typography.titleMedium
            )
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
            Spacer(modifier = Modifier.height(6.dp))
            PostContentIcons(feed = feed)
        }
    }
}

@Composable
fun PostContentIcons(
    feed: FeedItem,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth(0.7f)
    ) {
        ContentIcons.entries.forEach { item ->
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Top,
                modifier = Modifier
            ) {
                Icon(
                    modifier = Modifier.scale(0.7f).clickable { },
                    imageVector = item.icon,
                    contentDescription = ""
                )
                var count: String = item.getIconCount(feed, item).let {
                    if (it == "0") ""
                    else it
                }
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = count,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Right
                )
            }
        }
    }
}

enum class ContentIcons(val icon: ImageVector) {
    ChatBubbleOutline(Icons.Default.ChatBubbleOutline),
    Repeat(Icons.Default.Repeat),
    FavoriteBorder(Icons.Sharp.FavoriteBorder)
    ;

    fun getIconCount(feed: FeedItem, contentIcons: ContentIcons): String {
        return when (contentIcons) {
            ChatBubbleOutline -> feed.reply?.root?.jsonObject?.get("replyCount")?.jsonPrimitive?.content
                ?: ""

            Repeat -> feed.reply?.root?.jsonObject?.get("repostCount")?.jsonPrimitive?.content ?: ""
            FavoriteBorder -> feed.reply?.root?.jsonObject?.get("likeCount")?.jsonPrimitive?.content
                ?: ""
        }
    }
}
