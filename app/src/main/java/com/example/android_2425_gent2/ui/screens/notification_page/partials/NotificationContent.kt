package com.example.android_2425_gent2.ui.screens.notification_page.partials

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android_2425_gent2.R
import com.example.android_2425_gent2.data.model.Notification
import com.example.android_2425_gent2.extensions.formatRelative
import java.util.Date

@Composable
fun NotificationContent(
    notification: Notification,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    getSeverityIcon(notification.severity),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = getSeverityColor(notification.severity)
                )
                Text(
                    text = notification.title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.testTag("NotificationDetailsTitle")
                )
            }

            Text(
                text = notification.timeStamp.formatRelative(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.testTag("NotificationDetailsTimestamp")
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = notification.message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.testTag("NotificationDetailsMessage")
        )
    }
}

@Composable
private fun getSeverityIcon(severity: Int): ImageVector {
    return when (severity) {
        1 -> Icons.Rounded.Info // Info
        2 -> Icons.Rounded.CheckCircle // Success
        3 -> Icons.Rounded.Warning // Warning
        4 -> Icons.Rounded.Warning // Error
        else -> Icons.Rounded.Info // Default
    }
}

@Composable
private fun getSeverityColor(severity: Int): Color {
    return when (severity) {
        1 -> colorResource(id = R.color.info) // Info
        2 -> colorResource(id = R.color.success) // Success
        3 -> colorResource(id = R.color.warning) // Warning
        4 -> colorResource(id = R.color.error) // Error
        else -> colorResource(id = R.color.info) // Default
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationContentPreview() {
    val sampleNotification = Notification(
        id = 1,
        title = "System Update Available",
        message = "A new system update is available for your device. This update includes important security patches and performance improvements. Please ensure your device is connected to WiFi and has sufficient battery before starting the update process.",
        severity = 1, // Info severity
        isRead = false,
        timeStamp = Date()
    )

    NotificationContent(notification = sampleNotification)
}

@Preview(showBackground = true)
@Composable
fun NotificationContentSuccessPreview() {
    val sampleNotification = Notification(
        id = 2,
        title = "Backup Completed",
        message = "Your system backup has been completed successfully. All your files and settings have been safely stored in the cloud. You can access them anytime from your account settings.",
        severity = 2, // Success severity
        isRead = true,
        timeStamp = Date()
    )

    NotificationContent(notification = sampleNotification)
}

@Preview(showBackground = true)
@Composable
fun NotificationContentWarningPreview() {
    val sampleNotification = Notification(
        id = 3,
        title = "Storage Space Low",
        message = "Your device is running low on storage space. Please free up some space by removing unused apps or files to ensure optimal performance of your device.",
        severity = 3, // Warning severity
        isRead = false,
        timeStamp = Date()
    )

    NotificationContent(notification = sampleNotification)
}

@Preview(showBackground = true)
@Composable
fun NotificationContentErrorPreview() {
    val sampleNotification = Notification(
        id = 4,
        title = "Connection Error",
        message = "Unable to connect to the server. Please check your internet connection and try again. If the problem persists, contact support for assistance.",
        severity = 4, // Error severity
        isRead = true,
        timeStamp = Date()
    )

    NotificationContent(notification = sampleNotification)
}