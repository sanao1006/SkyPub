package app.skypub.data.repository.impl

import app.skypub.data.repository.NotificationRepository
import app.skypub.network.BlueskyApiDataSource

class NotificationRepositoryImpl(
    private val blueskyApiDataSource: BlueskyApiDataSource
) : NotificationRepository {
    override suspend fun getListNotifications(
        limit: Int?,
        priority: Boolean?,
        cursor: String?,
    ) = blueskyApiDataSource.getListNotifications(limit, priority, cursor)
}