package com.example.android_2425_gent2.data.mock_data

import com.example.android_2425_gent2.data.model.Notification
import java.util.Date

fun getMockNotifications(): List<Notification> {
    val date = Date()
    return (1..20).map {
        Notification(
            id = it,
            severity = getSeverity(it),
            title = "Title " + it,
            message = "Message",
            timeStamp = date,
            isRead = (it % 2 == 0)
        )
    }.toList<Notification>()
}

private fun getSeverity(severityId: Int): Int {
    return if(severityId % 4 == 0) {
        4;
    } else if(severityId % 3 == 0) {
        3
    } else if (severityId % 2 == 0) {
        2
    } else {
        1
    }
}