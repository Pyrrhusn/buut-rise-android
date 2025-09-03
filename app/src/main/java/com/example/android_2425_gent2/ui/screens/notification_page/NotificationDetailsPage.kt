package com.example.android_2425_gent2.ui.screens.notification_page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android_2425_gent2.R
import com.example.android_2425_gent2.data.model.Notification
import com.example.android_2425_gent2.ui.AppViewModelProvider
import com.example.android_2425_gent2.ui.screens.notification_page.partials.NotificationContent
import com.example.android_2425_gent2.ui.theme.Android2425gent2Theme
import java.util.Date

class NotificationPreviewParameterProvider : PreviewParameterProvider<Notification> {
    override val values = sequenceOf(
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
            title = "Welcome to the Platform!",
            message = "Get started by exploring our features and completing your first task.",
            timeStamp = Date(),
            isRead = true
        )
    )
}

@Preview(showBackground = true)
@Composable
fun NotificationDetailsContent_Preview(
    @PreviewParameter(NotificationPreviewParameterProvider::class) notification: Notification
) {
    Android2425gent2Theme {
        NotificationDetailsContent(
            notification = notification,
            modifier = Modifier
        )
    }
}

@Preview(showBackground = true, name = "Loading State")
@Composable
fun NotificationPageContent_LoadingPreview() {
    Android2425gent2Theme {
        NotificationPageContent(
            notificationsUiState = NotificationsUiState(loading = true),
            onNotificationClick = {},
            modifier = Modifier
        )
    }
}

@Preview(showBackground = true, name = "Error State")
@Composable
fun NotificationPageContent_ErrorPreview() {
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

@Preview(showBackground = true, name = "Empty State")
@Composable
fun NotificationPageContent_EmptyPreview() {
    Android2425gent2Theme {
        NotificationPageContent(
            notificationsUiState = NotificationsUiState(loading = false),
            onNotificationClick = {},
            modifier = Modifier
        )
    }
}

@Preview(showBackground = true, name = "With Notifications")
@Composable
fun NotificationPageContent_WithNotificationsPreview() {
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

@Composable
fun NotificationDetailsContent(
    notification: Notification,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        NotificationContent(
            notification = notification,
            modifier = Modifier.padding(16.dp)
        )
    }
}