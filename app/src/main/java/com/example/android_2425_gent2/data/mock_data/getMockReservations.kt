package com.example.android_2425_gent2.data.mock_data

import com.example.android_2425_gent2.data.model.Battery
import com.example.android_2425_gent2.data.model.Boat
import com.example.android_2425_gent2.data.model.Reservation
import com.example.android_2425_gent2.data.model.TimeSlot
import java.time.LocalDate
import java.time.LocalTime

fun getMockReservations(): List<Reservation> {
    return (1..20).map {
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
                date = if (it % 2 == 0) LocalDate.now()
                    .plusDays(it.toLong()) else LocalDate.now().minusDays(it.toLong()),
                start = LocalTime.of(9 + (it * 3) % 13, 0),
                end = LocalTime.of(9 + (it * 3) % 13, 0).plusHours(3)
            )
        )
    }.toList<Reservation>()

}