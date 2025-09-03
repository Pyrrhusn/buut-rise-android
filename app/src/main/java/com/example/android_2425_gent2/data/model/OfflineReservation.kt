package com.example.android_2425_gent2.data.model

import java.time.LocalDate
import java.time.LocalTime

data class OfflineReservation (
    val start: LocalTime,
    val end: LocalTime,
    val date: LocalDate,
    val boatId: Int,
    val boatPersonalName: String,
    val id: Int,
    val isDeleted: Boolean = false
)




