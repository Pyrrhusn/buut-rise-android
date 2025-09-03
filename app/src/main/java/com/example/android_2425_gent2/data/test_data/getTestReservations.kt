package com.example.android_2425_gent2.data.test_data

import com.example.android_2425_gent2.data.model.Battery
import com.example.android_2425_gent2.data.model.Boat
import com.example.android_2425_gent2.data.model.Reservation
import com.example.android_2425_gent2.data.model.TimeSlot
import java.time.LocalDate
import java.time.LocalTime

val testDate = LocalDate.of(2024, 10, 1)
val testTime = LocalTime.of(0,0)

fun getTestReservations(): List<Reservation> {
    return (1..3).map {
        Reservation(
            id = it,
            boat = Boat(
                id = it,
                name = "Boat $it"
            ),
            battery = Battery(
                id = it
            ),
            timeSlot = TimeSlot(
                id = it,
                date = testDate.plusDays(it.toLong() - 1),
                start = testTime,
                end = testTime.plusHours(3)
            )
        )
    }.toList<Reservation>()
}