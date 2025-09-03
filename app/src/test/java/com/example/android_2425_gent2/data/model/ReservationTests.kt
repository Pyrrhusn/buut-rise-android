package com.example.android_2425_gent2.data.model

import com.example.android_2425_gent2.data.local.entity.ReservationEntity
import com.example.android_2425_gent2.data.test_data.getTestReservations
import org.junit.Assert.assertEquals
import org.junit.Test


class ReservationTests {
    val reservation = getTestReservations()[0]

    @Test
    fun reservationAsEntity_returnsReservationEntity() {
        val reservationEntity = reservation.asEntity()

        assertEquals(
            reservationEntity, ReservationEntity(
                reservationId = reservation.id,
                boatId = reservation.boat?.id,
                batteryId = reservation.battery?.id,
                timeSlotId = reservation.timeSlot?.id,
            )
        )
    }
}