package com.example.android_2425_gent2.extensions

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun Date.formatRelative(): String {
    val now = Date()
    val formatter = SimpleDateFormat("h:mm a", Locale.getDefault())
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val dayFormatter = SimpleDateFormat("EEEE", Locale.getDefault())

    val calendarThis = Calendar.getInstance().apply { time = this@formatRelative }
    val calendarNow = Calendar.getInstance().apply { time = now }

    val isToday = isSameDay(calendarThis, calendarNow)
    val isYesterday = isYesterday(calendarThis, calendarNow)

    return when {
        isToday -> formatter.format(this)
        isYesterday -> "Yesterday ${formatter.format(this)}"
        isWithinLastWeek(calendarThis, calendarNow) -> "${dayFormatter.format(this)} ${formatter.format(this)}"
        else -> dateFormatter.format(this)
    }
}

private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}

private fun isYesterday(cal1: Calendar, cal2: Calendar): Boolean {
    val yesterday = Calendar.getInstance().apply {
        time = cal2.time
        add(Calendar.DAY_OF_YEAR, -1)
    }
    return isSameDay(cal1, yesterday)
}

private fun isWithinLastWeek(cal1: Calendar, cal2: Calendar): Boolean {
    val weekAgo = Calendar.getInstance().apply {
        time = cal2.time
        add(Calendar.DAY_OF_YEAR, -7)
    }
    return cal1.after(weekAgo) && cal1.before(cal2)
}

fun LocalDateTime.toDate(): Date {
    return Date.from(this.atZone(ZoneId.systemDefault()).toInstant())
}