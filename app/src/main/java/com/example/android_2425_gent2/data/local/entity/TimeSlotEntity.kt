package com.example.android_2425_gent2.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android_2425_gent2.data.model.TimeSlot
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "time_slot")
data class TimeSlotEntity(
    @PrimaryKey(autoGenerate = true)
    val timeSlotId: Int,
    val date: LocalDate,
    val start: LocalTime,
    val end: LocalTime,
)

fun TimeSlotEntity.asExternalModel() = TimeSlot(timeSlotId, date, start, end)
