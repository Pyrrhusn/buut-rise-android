package com.example.android_2425_gent2.ui.screens.notification_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_2425_gent2.data.model.Notification
import com.example.android_2425_gent2.data.repository.APIResource
import com.example.android_2425_gent2.data.repository.notification.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotificationViewModel(private val notificationRepository: NotificationRepository): ViewModel() {
    private val _notificationsUiState = MutableStateFlow(NotificationsUiState(loading = true))
    val notificationsUiState: StateFlow<NotificationsUiState> = _notificationsUiState

    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> = _unreadCount.asStateFlow()

    private val _selectedNotification = MutableStateFlow<Notification?>(null)
    val selectedNotification: StateFlow<Notification?> = _selectedNotification.asStateFlow()

    init {
        viewModelScope.launch {
            notificationRepository.notifications
                .collect { apiResource ->
                    updateUiState(apiResource)
                }
        }
        loadNotifications()
    }

    fun selectNotification(notification: Notification) {
        _selectedNotification.value = notification
        markAsRead(notification.id)
    }

    fun clearSelectedNotification() {
        _selectedNotification.value = null
    }

    private fun updateUiState(apiResource: APIResource<List<Notification>>) {
        when (apiResource) {
            is APIResource.Loading -> {
                _notificationsUiState.value = NotificationsUiState(loading = true)
            }
            is APIResource.Success -> {
                val response = apiResource.data
                if (response != null) {
                    _notificationsUiState.value = NotificationsUiState(
                        loading = false,
                        notifications = response.sortedByDescending { it.timeStamp }
                    )
                    // Update unread count
                    _unreadCount.value = response.count { !it.isRead }
                } else {
                    _notificationsUiState.value = NotificationsUiState(
                        hasError = true,
                        errorMessage = "No data available"
                    )
                }
            }
            is APIResource.Error -> {
                _notificationsUiState.value = NotificationsUiState(
                    notifications = _notificationsUiState.value.notifications,
                    hasError = true,
                    errorMessage = apiResource.message
                )
            }
        }
    }

    private fun loadNotifications() {
        viewModelScope.launch {
            notificationRepository.getNotifications().collect(::updateUiState)
        }
    }

    private fun markAsRead(notificationId: Int) {
        viewModelScope.launch {
            notificationRepository.markNotificationAsRead(notificationId)
                .collect { apiResource ->
                    when (apiResource) {
                        is APIResource.Error -> {
                            // Handle error if needed
                        }
                        is APIResource.Loading -> {
                            // We don't need to show loading state for mark as read
                        }
                        is APIResource.Success -> {
                            // No need to manually reload - shared flow will handle updates
                        }
                    }
                }
        }
    }
}

data class NotificationsUiState(
    val notifications: List<Notification> = emptyList(),
    val loading: Boolean = false,
    val hasError: Boolean = false,
    val errorMessage: String? = null
)