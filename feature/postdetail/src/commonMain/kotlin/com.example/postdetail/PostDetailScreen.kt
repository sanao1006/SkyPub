package com.example.postdetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.skypub.composables.ContentIcons
import app.skypub.navigation.UserScreen
import app.skypub.network.model.Post
import app.skypub.network.model.Reply
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ComposableImageRequest
import io.github.aakira.napier.Napier
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class PostDetailScreen(
    private val post: Post,
    private val reply: Reply?
) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        Napier.d(tag = "ray") { "reply $reply" }
        val navigator = LocalNavigator.currentOrThrow
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Thread") },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                )
            }
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 8.dp).padding(it)
            ) {
                PostDetailHeader(navigator = navigator)
                Spacer(modifier = Modifier.height(8.dp))
                PostDetailContent()
                HorizontalDivider()
                Spacer(modifier = Modifier.height(12.dp))
                PostDetailActions()
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider()
            }
        }
    }

    @Composable
    fun PostDetailHeader(
        modifier: Modifier = Modifier,
        navigator: Navigator
    ) {
        val userDetailScreen = rememberScreen(
            UserScreen.UserDetail(post.author.handle)
        )
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AsyncImage(
                request = ComposableImageRequest(uri = post.author.avatar),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(48.dp)
                    .clickable { navigator.push(userDetailScreen) },
            )
            Spacer(modifier = Modifier.size(16.dp))
            Column(modifier = Modifier.wrapContentHeight()) {
                Text(
                    text = post.author.displayName,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = post.author.handle,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = { /* follow */ }) {
                Text(text = "Follow")
            }
        }
    }

    @Composable
    fun PostDetailContent(modifier: Modifier = Modifier) {
        Column(modifier) {
            Text(
                text = post.record.jsonObject["text"]?.jsonPrimitive?.content ?: "",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            // TODO Display Date

        }
    }

    @Composable
    fun PostDetailActions(modifier: Modifier = Modifier) {
        Row(
            modifier = modifier.fillMaxWidth(0.7f).padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ContentIcons.entries.forEachIndexed { index, item ->
                Row {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.name,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = when (item) {
                            ContentIcons.ChatBubbleOutline -> {
                                val replyCount =
                                    reply?.root?.jsonObject?.get("replyCount")?.jsonPrimitive?.content
                                replyCount?.let { "$replyCount" } ?: ""
                            }

                            ContentIcons.Repeat -> {
                                val repostCount =
                                    reply?.root?.jsonObject?.get("repostCount")?.jsonPrimitive?.content

                                repostCount?.let { "$repostCount" } ?: ""
                            }


                            ContentIcons.FavoriteBorder -> {
                                val likeCount =
                                    reply?.root?.jsonObject?.get("likeCount")?.jsonPrimitive?.content

                                likeCount?.let { "$likeCount" } ?: ""
                            }
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}