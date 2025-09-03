package com.example.android_2425_gent2.data.local.entity.linking_entities

import com.example.android_2425_gent2.data.local.entity.BoatEntity
import com.example.android_2425_gent2.data.local.entity.ReservationEntity
import com.example.android_2425_gent2.data.local.entity.TimeSlotEntity
import com.example.android_2425_gent2.data.model.Boat
import com.example.android_2425_gent2.data.model.Reservation
import com.example.android_2425_gent2.data.model.TimeSlot
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class ReservationWithTimeSlotAndBoatEntityTests {
    @Test
    fun entityAsExternalModel_returnsReservation() {
        val entity = ReservationWithTimeSlotAndBoatEntity(
            reservation = ReservationEntity(
                reservationId = 1,
                boatId = 1,
                batteryId = 1,
                timeSlotId = 1
            ),
            timeSlot = TimeSlotEntity(
                timeSlotId = 1,
                date = LocalDate.of(2024, 1, 1),
                start = LocalTime.of(0, 0),
                end = LocalTime.of(0, 0)
            ),
            boat = BoatEntity(
                boatId = 1,
                name = "Boat 1"
            )
        )
        val reservation = Reservation(
            id = 1,
            boat = Boat(
                id = 1,
                name = "Boat 1"
            ),
            battery = null,
            timeSlot = TimeSlot(
                id = 1,
                date = LocalDate.of(2024, 1, 1),
                start = LocalTime.of(0, 0),
                end = LocalTime.of(0, 0)
            )
        )
        assertEquals(
            reservation, entity.asExternalModel()
        )
    }
}
