package app.skypub.network.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

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
        avatar = author.avatar,
        record = record
    )
}

data class NotificationDomainModel(
    val reason: String,
    val name: String,
    val isRead: Boolean,
    val avatar: String? = null,
    val record: JsonElement? = null,
)