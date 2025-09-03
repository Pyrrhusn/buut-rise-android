package com.example.android_2425_gent2.data.local.entity

import com.example.android_2425_gent2.data.model.Notification
import com.example.android_2425_gent2.extensions.toDate
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.LocalDateTime

class NotificationEntityTest {

    @Test
    fun entityAsExternalModel_returnsNotification() {

        val createdAt = LocalDateTime.now()

        val entity = OfflineNotificationEntity(
            id = 1,
            title = "Test Notification 1",
            message = "Test Message 1",
            severity = 1,
            createdAt = createdAt,
            isRead = false
        )

        val notification = Notification(
            id = 1,
            title = "Test Notification 1",
            message = "Test Message 1",
            severity = 1,
            timeStamp = createdAt.toDate(),
            isRead = false,
        )

        assertEquals(entity.asExternalModel(), notification)
    }
}