package com.example.android_2425_gent2.data.local.entity.linking_entities

import androidx.room.Embedded
import androidx.room.Relation
import com.example.android_2425_gent2.data.local.entity.ReservationEntity
import com.example.android_2425_gent2.data.local.entity.TimeSlotEntity
import com.example.android_2425_gent2.data.local.entity.asExternalModel
import com.example.android_2425_gent2.data.model.Reservation


data class ReservationWithTimeSlotEntity(
    @Embedded val reservation: ReservationEntity,
    @Relation(
        parentColumn = "reservationId",
        entityColumn = "timeSlotId",
    )
    val timeSlot: TimeSlotEntity?
)

fun ReservationWithTimeSlotEntity.asExternalModel() = Reservation(
    id = reservation.reservationId,
    boat = null,
    battery = null,
    timeSlot = timeSlot?.asExternalModel(),
)
