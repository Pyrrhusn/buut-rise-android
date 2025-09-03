package com.example.android_2425_gent2.data.local.entity.linking_entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.android_2425_gent2.data.local.entity.ReservationEntity
import com.example.android_2425_gent2.data.local.entity.UserEntity
import com.example.android_2425_gent2.data.local.entity.UserReservationCrossRef

data class UserWithReservationsEntity(
    @Embedded val user: UserEntity,
    @Relation(
        parentColumn = "userId",
        entityColumn = "reservationId",
        associateBy = Junction(UserReservationCrossRef::class)
    )
    val reservations: List<ReservationEntity>
)
