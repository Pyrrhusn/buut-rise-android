package com.example.android_2425_gent2.data.remote.model

data class TimeSlotResponse(
    val start: String,
    val end: String,
    val totalDays: Int,
    val days: List<DayInfo>
)

data class DayInfo(
    val date: String,
    val isFullyBooked: Boolean,
    val isSlotAvailable: Boolean
)