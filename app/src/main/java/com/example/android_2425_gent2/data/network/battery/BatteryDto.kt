package com.example.android_2425_gent2.data.network.battery

data class BatteryDto(
    val id: Int,
    val type: String,
    val mentor: Mentor?
)

data class Mentor(
    val id: Int,
    val fullName: String,
    val firstName: String,
    val familyName: String
)