package com.example.android_2425_gent2.ui.screens.notification_page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android_2425_gent2.R
import com.example.android_2425_gent2.data.model.Notification
import com.example.android_2425_gent2.ui.screens.notification_page.partials.NotificationRow
import com.example.android_2425_gent2.ui.theme.Android2425gent2Theme
import java.util.Date

@Composable
fun NotificationPageContent(
    notificationsUiState: NotificationsUiState,
    onNotificationClick: (Notification) -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        notificationsUiState.hasError -> {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = notificationsUiState.errorMessage
                        ?: stringResource(R.string.something_went_wrong),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        notificationsUiState.loading && notificationsUiState.notifications.isEmpty() -> {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }
        notificationsUiState.notifications.isEmpty() -> {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No notifications",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
        else -> {
            LazyColumn(
                modifier = modifier.testTag("NotificationPageList"),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(notificationsUiState.notifications) { notification ->
                    NotificationRow(
                        notification = notification,
                        onClick = { onNotificationClick(notification) }
                    )
                }
            }
        }
    }
}

// Previous Loading Preview
@Preview(showBackground = true, name = "Notifications - Loading State")
@Composable
fun NotificationPageContent_LoadingStatePreview() {
    Android2425gent2Theme {
        NotificationPageContent(
            notificationsUiState = NotificationsUiState(loading = true),
            onNotificationClick = {},
            modifier = Modifier
        )
    }
}

@Preview(showBackground = true, name = "Notifications - Error State")
@Composable
fun NotificationPageContent_ErrorStatePreview() {
    Android2425gent2Theme {
        NotificationPageContent(
            notificationsUiState = NotificationsUiState(
                hasError = true,
                errorMessage = "Something went wrong. Please try again."
            ),
            onNotificationClick = {},
            modifier = Modifier
        )
    }
}

@Preview(showBackground = true, name = "Notifications - Empty State")
@Composable
fun NotificationPageContent_EmptyStatePreview() {
    Android2425gent2Theme {
        NotificationPageContent(
            notificationsUiState = NotificationsUiState(loading = false),
            onNotificationClick = {},
            modifier = Modifier
        )
    }
}

@Preview(showBackground = true, name = "Notifications - With Content")
@Composable
fun NotificationPageContent_WithContentPreview() {
    val notifications = listOf(
        Notification(
            id = 1,
            severity = 1,
            title = "Complete Your Profile",
            message = "Your profile is incomplete. Add more information to unlock all features.",
            timeStamp = Date(),
            isRead = false
        ),
        Notification(
            id = 2,
            severity = 2,
            title = "Welcome!",
            message = "Get started by exploring our features.",
            timeStamp = Date(),
            isRead = true
        ),
        Notification(
            id = 3,
            severity = 3,
            title = "New Update Available",
            message = "Update your app to access the latest features.",
            timeStamp = Date(),
            isRead = false
        )
    )

    Android2425gent2Theme {
        NotificationPageContent(
            notificationsUiState = NotificationsUiState(
                notifications = notifications,
                loading = false
            ),
            onNotificationClick = {},
            modifier = Modifier
        )
    }
}