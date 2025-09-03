package com.example.android_2425_gent2.data.model

import com.example.android_2425_gent2.data.local.entity.BoatEntity

data class Boat(
    val id: Int,
    val name: String,
)

fun Boat.asEntity() = BoatEntity(id, name)

