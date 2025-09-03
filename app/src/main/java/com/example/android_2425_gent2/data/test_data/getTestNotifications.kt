package com.example.android_2425_gent2.data.test_data

import com.example.android_2425_gent2.data.model.Notification
import java.util.Date

    fun getTestNotifications(): List<Notification> {
        val timeStamp = Date()
        return (1..4).map {
            Notification(
                id = it,
                title = "Title " + it.toString(),
                message = "Message " + it.toString(),
                severity = it,
                isRead = false,
                timeStamp = timeStamp
            )
        }.toList<Notification>()
    }
