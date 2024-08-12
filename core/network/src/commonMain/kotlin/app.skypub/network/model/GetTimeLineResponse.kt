package app.skypub.network.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement


@Serializable
data class GetTimeLineResponse(
    val cursor: String,
    val feed: List<FeedItem>
)


@Serializable
data class FeedItem(
    val post: Post,
    val reply: Reply? = null,
    val reason: JsonElement? = null,
    val feedContext: String? = null
)

@Serializable
data class Post(
    val uri: String,
    val cid: String,
    val author: Author,
    val record: JsonElement,
    val embed: JsonElement? = null,
    val replyCount: Int,
    val repostCount: Int,
    val likeCount: Int,
    val indexedAt: String,
    val viewer: PostViewer? = null,
    val labels: List<Label>? = null,
    val threadgate: ThreadGate? = null
)

@Serializable
data class Author(
    val did: String,
    val handle: String,
    val displayName: String,
    val avatar: String,
    val associated: Associated? = null,
    val viewer: Viewer? = null,
    val labels: List<Label>? = null,
    val createdAt: String? = null
)

@Serializable
data class Viewer(
    val muted: Boolean? = null,
    val mutedByList: MutedByList? = null,
    val blockedBy: Boolean? = null,
    val blocking: String? = null,
    val blockingByList: BlockingByList? = null,
    val following: String? = null,
    val followedBy: String? = null,
    val knownFollowers: KnownFollowers? = null,
)

@Serializable
data class Associated(
    val lists: Int? = null,
    val feedgens: Int? = null,
    val starterPacks: Int? = null,
    val labeler: Boolean? = null,
    val chat: Chat
)

@Serializable
data class Chat(
    val allowIncoming: String
)

@Serializable
data class PostViewer(
    val repost: String? = null,
    val like: String? = null,
    val threadMuted: Boolean? = null,
    val replyDisabled: Boolean? = null
)

@Serializable
data class MutedByList(
    val uri: String,
    val cid: String,
    val name: String,
    val purpose: String,
    val avatar: String? = null,
    val listItemCount: Int? = null,
    val labels: List<Label>? = null,
    val viewer: MutedOrBlockedByListViewer? = null,
    val indexedAt: String? = null
)

@Serializable
data class MutedOrBlockedByListViewer(
    val muted: Boolean,
    val blocked: String
)

@Serializable
data class BlockingByList(
    val uri: String,
    val cid: String,
    val name: String,
    val purpose: String,
    val avatar: String,
    val listItemCount: Int,
    val labels: List<Label>,
    val viewer: MutedOrBlockedByListViewer,
    val indexedAt: String
)

@Serializable
data class KnownFollowers(
    val count: Int,
    val followers: List<Follower>
)

@Serializable
data class Label(
    val ver: Int? = null,
    val src: String,
    val uri: String,
    val cid: String,
    val `val`: String,
    val neg: Boolean? = null,
    val cts: String,
    val exp: String? = null,
    val sig: String? = null
)

@Serializable
data class ThreadGate(
    val uri: String,
    val cid: String,
    val record: JsonElement,
    val lists: List<ThreadGateList>
)

@Serializable
data class ThreadGateList(
    val uri: String,
    val cid: String,
    val name: String,
    val purpose: String,
    val avatar: String,
    val listItemCount: Int,
    val labels: List<Label>,
    val viewer: ThreadGateListViewer,
    val indexedAt: String
)

@Serializable
data class ThreadGateListViewer(
    val muted: Boolean,
    val blocked: String
)

@Serializable
data class Reply(
    val root: JsonElement,
    val parent: JsonElement,
    val grandparentAuthor: Author? = null,
)