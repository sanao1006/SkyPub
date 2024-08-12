package app.skypub.network.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetListNotificationsResponse(
    @SerialName("cursor")
    val cursor: String,
    @SerialName("notifications")
    val notifications: List<Notification>,
    @SerialName("priority")
    val priority: Boolean,
    @SerialName("seenAt")
    val seenAt: String
)