package com.example.android_2425_gent2.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android_2425_gent2.data.model.Reservation

@Entity(tableName = "reservation")
data class ReservationEntity(
    @PrimaryKey(autoGenerate = true)
    val reservationId: Int,
    val boatId: Int?,
    val batteryId: Int?,
    val timeSlotId: Int?,
)

fun ReservationEntity.asExternalModel() = Reservation(
    id = reservationId,
    boat = null,
    battery = null,
    timeSlot = null,
)
