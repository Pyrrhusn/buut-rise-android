package com.example.android_2425_gent2.data.local.entity.linking_entities

import androidx.room.Embedded
import androidx.room.Relation
import com.example.android_2425_gent2.data.local.entity.BoatEntity
import com.example.android_2425_gent2.data.local.entity.ReservationEntity

data class ReservationWithBoatEntity(
    @Embedded val boat: BoatEntity,
    @Relation(
        parentColumn = "reservationId",
        entityColumn = "boatId",
    )
    val reservation: ReservationEntity
)
