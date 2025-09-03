package com.example.android_2425_gent2.data.model

import com.example.android_2425_gent2.data.local.entity.OfflineNotificationEntity
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.ZoneId
import java.util.Date

class NotificationTest {
    private val testDate = Date()
    private val notification = Notification(
        id = 1,
        severity = 1,
        title = "Test Title",
        message = "Test Message",
        timeStamp = testDate,
        isRead = false
    )

    @Test
    fun notificationAsEntity_returnsNotificationEntity() {
        val notificationEntity = notification.asEntity()

        assertEquals(
            notificationEntity,
            OfflineNotificationEntity(
                id = notification.id,
                title = notification.title,
                message = notification.message,
                severity = notification.severity,
                isRead = notification.isRead,
                createdAt = notification.timeStamp.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
            )
        )
    }
}