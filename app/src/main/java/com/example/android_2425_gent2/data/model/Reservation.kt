package com.example.android_2425_gent2.data.model

import com.example.android_2425_gent2.data.local.entity.ReservationEntity

data class Reservation(
    val id: Int,
    val boat: Boat?,
    val battery: Battery?,
    val timeSlot: TimeSlot?,
)

fun Reservation.asEntity() = ReservationEntity(id, boat?.id, battery?.id, timeSlot?.id)

