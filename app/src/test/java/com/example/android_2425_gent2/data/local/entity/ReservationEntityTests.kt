package com.example.android_2425_gent2.data.local.entity

import com.example.android_2425_gent2.data.model.Reservation
import junit.framework.TestCase.assertEquals
import org.junit.Test

class ReservationEntityTests {
    @Test
    fun entityAsExternalModel_returnsReservation() {
        val entity = ReservationEntity(
            reservationId = 1,
            boatId = 1,
            batteryId = 1,
            timeSlotId = 1
        )
        val reservation = Reservation(
            id = 1,
            boat = null,
            battery = null,
            timeSlot = null
        )
        assertEquals(entity.asExternalModel(), reservation)
    }
}