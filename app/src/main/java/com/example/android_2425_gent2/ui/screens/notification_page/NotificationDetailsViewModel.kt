package com.example.android_2425_gent2.ui.screens.notification_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_2425_gent2.data.model.Notification
import com.example.android_2425_gent2.data.repository.APIResource
import com.example.android_2425_gent2.data.repository.notification.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotificationDetailsViewModel(
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    private val _notificationDetailsUiState = MutableStateFlow(NotificationDetailsUiState())
    val notificationDetailsUiState: StateFlow<NotificationDetailsUiState> = _notificationDetailsUiState

    fun setNotification(notification: Notification) {
        _notificationDetailsUiState.value = NotificationDetailsUiState(notification = notification)
        markAsRead(notification.id)
    }

    private fun markAsRead(notificationId: Int) {
        viewModelScope.launch {
            notificationRepository.markNotificationAsRead(notificationId)
                .collect { apiResource ->
                    when (apiResource) {
                        is APIResource.Error -> {
                            _notificationDetailsUiState.value = _notificationDetailsUiState.value.copy(
                                hasError = true,
                                errorMessage = apiResource.message
                            )
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

data class NotificationDetailsUiState(
    val notification: Notification? = null,
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
    val errorMessage: String? = null
)