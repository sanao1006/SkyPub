package app.skypub.network.model

import kotlinx.serialization.Serializable

@Serializable
data class GetAuthorFeedResponse(
    val cursor: String,
    val feed: List<FeedItem>
)