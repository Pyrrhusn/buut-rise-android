package com.example.android_2425_gent2.data.network.model

data class RemoteReservation(
    val id: Int,
    val start: String,
    val end: String,
    val date: String,
    val amountAdults: Int,
    val amountChildren: Int,
    val amountPets: Int
)
