package com.example.android_2425_gent2.data.repository.battery

import com.example.android_2425_gent2.data.network.battery.BatteryDto
import com.example.android_2425_gent2.data.repository.APIResource
import kotlinx.coroutines.flow.Flow

interface BatteryRepository {
    suspend fun getAllBatteries(): Flow<APIResource<List<BatteryDto>>>
    suspend fun getBatteriesByBoat(boatId: Int): Flow<APIResource<List<BatteryDto>>>
    suspend fun assignMentor(batteryId: Int, mentorId: Int): Flow<APIResource<Unit>>
} 