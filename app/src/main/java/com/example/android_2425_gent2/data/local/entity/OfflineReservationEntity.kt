package com.example.android_2425_gent2.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android_2425_gent2.data.model.OfflineReservation
import com.example.android_2425_gent2.data.network.model.ReservationDetailsDto
import com.example.android_2425_gent2.data.network.model.ReservationDto
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "offline_reservation")
data class OfflineReservationEntity(
    @PrimaryKey
    val id: Int,
    val start: LocalTime,
    val end: LocalTime,
    val date: LocalDate,
    val boatId: Int,
    val boatPersonalName: String,
    val isDeleted: Boolean = false,
    val mentorName: String? = null,
    val batteryId: Int? = null,
    val currentBatteryUserName: String? = null,
    val currentBatteryUserId: Int? = null,
    val currentHolderPhoneNumber: String? = null,
    val currentHolderEmail: String? = null,
    val currentHolderStreet: String? = null,
    val currentHolderNumber: String? = null,
    val currentHolderCity: String? = null,
    val currentHolderPostalCode: String? = null
)

fun OfflineReservationEntity.asExternalModel() = OfflineReservation(
    id = id,
    start = start,
    end = end,
    date = date,
    boatId = boatId,
    boatPersonalName = boatPersonalName,
    isDeleted = isDeleted
)

fun OfflineReservationEntity.asReservationDetails() = ReservationDetailsDto(
    id = id,
    start = start.toString(),
    end = end.toString(),
    date = date.toString(),
    isDeleted = isDeleted,
    boatPersonalName = boatPersonalName,
    mentorName = mentorName,
    batteryId = batteryId ?: 0,
    currentBatteryUserName = currentBatteryUserName,
    currentBatteryUserId = currentBatteryUserId ?: 0,
    currentHolderPhoneNumber = currentHolderPhoneNumber,
    currentHolderEmail = currentHolderEmail,
    currentHolderStreet = currentHolderStreet,
    currentHolderNumber = currentHolderNumber,
    currentHolderCity = currentHolderCity,
    currentHolderPostalCode = currentHolderPostalCode
)
