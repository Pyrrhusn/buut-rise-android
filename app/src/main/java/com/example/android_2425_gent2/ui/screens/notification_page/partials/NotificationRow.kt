package com.example.android_2425_gent2.ui.screens.notification_page.partials

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android_2425_gent2.R
import com.example.android_2425_gent2.data.model.Notification
import com.example.android_2425_gent2.extensions.formatRelative
import java.util.Date

@Composable
fun NotificationRow(
    notification: Notification,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("notification_${notification.id}")
    ) {
        Column(
            modifier = modifier.padding(16.dp)
        ) {
            // Top row with icon, title and time
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = modifier.weight(1f).testTag("row_content_${notification.id}"),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        getSeverityIcon(notification.severity),
                        contentDescription = null,
                        modifier = modifier.size(24.dp),
                        tint = getSeverityColor(notification.severity)
                    )
                    Spacer(modifier = modifier.width(8.dp))
                    Text(
                        text = notification.title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (!notification.isRead) {
                    Spacer(modifier = modifier.width(4.dp))
                    Box(
                        modifier = modifier
                            .size(8.dp)
                            .background(colorResource(id = R.color.info), CircleShape)
                            .testTag("unread_indicator_${notification.id}")
                            .semantics(mergeDescendants = false) {}
                    )
                }
                Text(
                    text = notification.timeStamp.formatRelative(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )

            }

            // Message
            Spacer(modifier = modifier.height(8.dp))
            Text(
                text = notification.message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
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
fun NotificationRowPreview() {
    val sampleNotification = Notification(
        id = 1,
        title = "Notification Title that might be longer than one line to demonstrate overflow",
        message = "This is a sample notification message that might be longer than one line to demonstrate overflow",
        severity = 1, // Info severity
        isRead = false,
        timeStamp = Date()
    )

    NotificationRow(
        notification = sampleNotification,
        onClick = { /* Preview click handler */ }
    )
}


@Preview(showBackground = true)
@Composable
fun NotificationRowPreviewRead() {
    val sampleNotification = Notification(
        id = 2,
        title = "Unread warning notification",
        message = "This is a read notification with warning severity",
        severity = 3, // Warning severity
        isRead = true,
        timeStamp = Date()
    )

    NotificationRow(
        notification = sampleNotification,
        onClick = { /* Preview click handler */ }
    )
}

@Preview(showBackground = true)
@Composable
fun NotificationRowPreviewSuccess() {
    val sampleNotification = Notification(
        id = 3,
        title = "Success notification",
        message = "This is a success notification",
        severity = 2, // Success severity
        isRead = false,
        timeStamp = Date()
    )

    NotificationRow(
        notification = sampleNotification,
        onClick = { /* Preview click handler */ }
    )
}