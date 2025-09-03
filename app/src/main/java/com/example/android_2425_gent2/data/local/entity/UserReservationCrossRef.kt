package com.example.android_2425_gent2.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    primaryKeys = ["userId", "reservationId"], tableName = "user_reservation",
    foreignKeys =
    [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.Companion.CASCADE,
            onUpdate = ForeignKey.Companion.CASCADE,
        ),
        ForeignKey(
            entity = ReservationEntity::class,
            parentColumns = ["reservationId"],
            childColumns = ["reservationId"],
            onDelete = ForeignKey.Companion.CASCADE,
            onUpdate = ForeignKey.Companion.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["reservationId"]),
        Index(value = ["userId"]),
    ],
)
data class UserReservationCrossRef(
    val userId: Int,
    val reservationId: Int,
)
