package com.example.android_2425_gent2.data.network.model

data class ReservationDetailsDto(
    val id: Int,
    val start: String,
    val end: String,
    val date: String,
    val isDeleted: Boolean,
    val boatPersonalName: String,
    val mentorName: String?,
    val batteryId: Int,
    val currentBatteryUserName: String?,
    val currentBatteryUserId: Int,
    val currentHolderPhoneNumber: String?,
    val currentHolderEmail: String?,
    val currentHolderStreet: String?,
    val currentHolderNumber: String?,
    val currentHolderCity: String?,
    val currentHolderPostalCode: String?
) 