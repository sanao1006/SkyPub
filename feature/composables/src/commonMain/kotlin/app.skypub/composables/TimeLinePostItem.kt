package app.skypub.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.sharp.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.skypub.navigation.PostDetailScreen
import app.skypub.navigation.ReplyScreen
import app.skypub.navigation.UserScreen
import app.skypub.network.model.FeedItem
import app.skypub.network.model.Ref
import app.skypub.network.model.ReplyRef
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.navigator.Navigator
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ComposableImageRequest
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Composable
fun TimeLinePostItem(
    navigator: Navigator,
    feed: FeedItem,
    modifier: Modifier = Modifier,
    onIconClick: (ContentIcons, String, String, String) -> Unit
) {
    val userDetailScreen = rememberScreen(
        UserScreen.UserDetail(feed.post.author.handle)
    )
    val postDetailScreen = rememberScreen(
        PostDetailScreen.PostDetail(
            post = feed.post,
            reply = feed.reply
        )
    )
    val ref = Ref(
        uri = feed.post.uri,
        cid = feed.post.cid
    )
    val replyRef = if (feed.reply == null) {
        ReplyRef(
            parent = ref,
            root = ref
        )
    } else {
        ReplyRef(
            parent = ref,
            root = Ref(
                uri = feed.reply!!.root.jsonObject["uri"]?.jsonPrimitive?.content ?: "",
                cid = feed.reply!!.root.jsonObject["cid"]?.jsonPrimitive?.content ?: ""
            )
        )
    }
    val replyScreen = rememberScreen(
        ReplyScreen.Reply(
            name = feed.post.author.displayName,
            handle = feed.post.author.handle,
            thumbnail = feed.post.author.avatar,
            post = feed.post.record.jsonObject["text"]?.jsonPrimitive?.content ?: "",
            ref = replyRef
        )
    )
    Row(modifier = modifier.clickable { navigator.push(postDetailScreen) }) {
        AsyncImage(
            modifier = Modifier.size(40.dp).clip(CircleShape)
                .clickable(onClick = { navigator.push(userDetailScreen) }),
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
            TimeLinePostContentIcons(
                feed = feed,
                onFavIconClick = onIconClick,
                onReplyIconClick = { navigator.push(replyScreen) }
            )
        }
    }
}

@Composable
private fun TimeLinePostContentIcons(
    feed: FeedItem,
    onFavIconClick: (ContentIcons, String, String, String) -> Unit,
    onReplyIconClick: () -> Unit,
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
                    modifier = Modifier.scale(0.8f)
                        .clickable {
                            when (item) {
                                ContentIcons.FavoriteBorder -> {
                                    onFavIconClick(
                                        item,
                                        feed.post.author.did,
                                        feed.post.uri,
                                        feed.post.cid
                                    )
                                }

                                ContentIcons.ChatBubbleOutline -> {
                                    onReplyIconClick()
                                }

                                else -> {}
                            }
                        },
                    painter = rememberVectorPainter(item.getIconState(feed.post.viewer?.like)),
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

enum class ContentIcons(val icon: ImageVector, val filledIcon: ImageVector?) {
    ChatBubbleOutline(Icons.Default.ChatBubbleOutline, null),
    Repeat(Icons.Default.Repeat, Icons.Filled.Repeat),
    FavoriteBorder(Icons.Sharp.FavoriteBorder, Icons.Filled.Favorite)
    ;

    fun getIconState(like: String?): ImageVector {
        return when (this) {
            FavoriteBorder -> if (like.isNullOrBlank()) icon else filledIcon ?: icon
            else -> icon
        }
    }

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
