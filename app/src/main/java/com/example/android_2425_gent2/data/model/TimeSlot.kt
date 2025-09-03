package com.example.android_2425_gent2.data.model

import com.example.android_2425_gent2.data.local.entity.TimeSlotEntity
import java.time.LocalDate
import java.time.LocalTime

data class TimeSlot(
    val id: Int,
    val date: LocalDate,
    val start: LocalTime,
    val end: LocalTime,
)

fun TimeSlot.asEntity() = TimeSlotEntity(id, date, start, end)

