package com.example.android_2425_gent2.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android_2425_gent2.data.model.Boat

@Entity(tableName = "boat")
data class BoatEntity(
    @PrimaryKey(autoGenerate = true)
    val boatId: Int,
    val name: String
)

fun BoatEntity.asExternalModel() = Boat(
    id = boatId,
    name = name,
)
