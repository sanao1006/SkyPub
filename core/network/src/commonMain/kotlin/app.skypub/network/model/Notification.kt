package app.skypub.network.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class Notification(
    @SerialName("author")
    val author: Author,
    @SerialName("cid")
    val cid: String,
    @SerialName("indexedAt")
    val indexedAt: String,
    @SerialName("isRead")
    val isRead: Boolean,
    @SerialName("labels")
    val labels: List<Label>,
    @SerialName("reason")
    val reason: String,
    @SerialName("reasonSubject")
    val reasonSubject: String,
    @SerialName("record")
    val record: JsonElement? = null,
    @SerialName("uri")
    val uri: String
) {
    fun toNotificationDomainModel() = NotificationDomainModel(
        reason = reason,
        name = author.displayName,
        isRead = isRead,
        createdAt = record?.jsonObject?.get("createdAt")?.jsonPrimitive?.content ?: "",
        handle = author.handle,
        avatar = author.avatar,
        record = record
    )
}

data class NotificationDomainModel(
    val reason: String,
    val name: String,
    val isRead: Boolean,
    val createdAt: String,
    val handle: String,
    val avatar: String? = null,
    val post: String? = null,
    val record: JsonElement? = null,
)