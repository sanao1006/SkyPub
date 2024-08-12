package app.skypub.data.repository

import app.skypub.network.model.GetListNotificationsResponse
import app.skypub.network.model.RequestErrorResponse
import arrow.core.Either

interface NotificationRepository {
    suspend fun getListNotifications(
        limit: Int? = null,
        priority: Boolean? = null,
        cursor: String? = null,
    ): Either<RequestErrorResponse, GetListNotificationsResponse>
}