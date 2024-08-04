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
    val embed: JsonElement,
    val replyCount: Int,
    val repostCount: Int,
    val likeCount: Int,
    val indexedAt: String,
    val viewer: Viewer,
    val labels: List<Label>,
    val threadgate: ThreadGate? = null
)

@Serializable
data class Author(
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
data class Associated(
    val lists: Int,
    val feedgens: Int,
    val starterPacks: Int,
    val labeler: Boolean,
    val chat: Chat
)

@Serializable
data class Chat(
    val allowIncoming: String
)

@Serializable
data class Viewer(
    val muted: Boolean,
    val mutedByList: MutedByList? = null,
    val blockedBy: Boolean,
    val blocking: String? = null,
    val blockingByList: BlockingByList? = null,
    val following: String? = null,
    val followedBy: String? = null,
    val knownFollowers: KnownFollowers? = null,
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
    val avatar: String,
    val listItemCount: Int,
    val labels: List<Label>,
    val viewer: MutedByListViewer,
    val indexedAt: String
)

@Serializable
data class MutedByListViewer(
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
    val viewer: BlockingByListViewer,
    val indexedAt: String
)

@Serializable
data class BlockingByListViewer(
    val muted: Boolean,
    val blocked: String
)

@Serializable
data class KnownFollowers(
    val count: Int,
    val followers: List<JsonElement?>
)

@Serializable
data class Label(
    val ver: Int,
    val src: String,
    val uri: String,
    val cid: String,
    val `val`: String,
    val neg: Boolean,
    val cts: String,
    val exp: String? = null,
    val sig: String
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
    val grandparentAuthor: Author
)