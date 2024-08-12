package app.skypub.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.skypub.data.repository.NotificationRepository
import app.skypub.network.model.NotificationDomainModel
import arrow.core.Either
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val notificationRepository: NotificationRepository
) : ViewModel() {
    private var _uiState: MutableStateFlow<NotificationScreenUiState> =
        MutableStateFlow(NotificationScreenUiState(notifications = emptyList()))
    val uiState = _uiState.asStateFlow()

    private fun fetchNotifications() {
        viewModelScope.launch {
            when (val result = notificationRepository.getListNotifications()) {
                is Either.Right -> {
                    _uiState.update { uiState ->
                        uiState.copy(notifications = result.value.notifications.map { it.toNotificationDomainModel() })
                    }
                }

                is Either.Left -> {
                    Napier.e(tag = "NotificationViewModel", message = "Error: ${result.value}")
                }
            }
        }
    }

    init {
        fetchNotifications()
    }
}

data class NotificationScreenUiState(
    var notifications: List<NotificationDomainModel>,
)