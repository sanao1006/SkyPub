package app.skypub.data.repository

import app.skypub.network.model.GetListNotificationsResponse
import app.skypub.network.model.RequestErrorResponse
import arrow.core.Either

interface NotificationRepository {
    suspend fun getListNotifications(
        limit: Int?,
        priority: Boolean?,
        cursor: String?,
    ): Either<RequestErrorResponse, GetListNotificationsResponse>
}