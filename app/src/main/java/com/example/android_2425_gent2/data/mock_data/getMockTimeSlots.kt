package com.example.android_2425_gent2.data.mock_data

import com.example.android_2425_gent2.data.model.TimeSlot
import java.time.LocalDate
import java.time.LocalTime

fun getMockTimeSlots(): List<TimeSlot> {
    return (1..20).map {
        TimeSlot(
            id = it,
            date = LocalDate.of(2024, 10, (it % 3) + 1),
            start = LocalTime.of(9 + (it * 3) % 13, 0),
            end = LocalTime.of(9 + (it * 3) % 13, 0).plusHours(3)
        )
    }
}