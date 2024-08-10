package app.skypub.network.model

import kotlinx.serialization.Serializable

@Serializable
data class GetProfileResponse(
    val did: String,
    val handle: String,
    val displayName: String,
    val description: String,
    val avatar: String,
    val banner: String? = null,
    val followersCount: Int,
    val followsCount: Int,
    val postsCount: Int,
    val associated: Associated,
    val joinedViaStarterPack: JoinedViaStarterPack? = null,
    val indexedAt: String,
    val createdAt: String,
    val viewer: Viewer,
    val labels: List<Label>
)

@Serializable
data class JoinedViaStarterPack(
    val uri: String,
    val cid: String,
    val record: Map<String, String>,
    val creator: Creator,
    val listItemCount: Int,
    val joinedWeekCount: Int,
    val joinedAllTimeCount: Int,
    val labels: List<Label>,
    val indexedAt: String
)

@Serializable
data class Creator(
    val did: String,
    val handle: String,
    val displayName: String,
    val avatar: String,
    val associated: Associated,
    val viewer: Viewer,
    val labels: List<Label>,
    val createdAt: String
)

@Serializable
data class Follower(
    val did: String,
    val handle: String,
    val displayName: String,
    val avatar: String,
    val associated: Associated,
    val labels: List<Label>,
    val createdAt: String
)
