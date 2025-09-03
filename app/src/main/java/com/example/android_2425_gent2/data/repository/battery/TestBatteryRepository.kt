package com.example.android_2425_gent2.data.repository.battery

import com.example.android_2425_gent2.data.network.battery.BatteryDto
import com.example.android_2425_gent2.data.network.battery.Mentor
import com.example.android_2425_gent2.data.repository.APIResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TestBatteryRepository : BatteryRepository {
    override suspend fun getAllBatteries(): Flow<APIResource<List<BatteryDto>>> = flow {
        emit(APIResource.Success(listOf(
            BatteryDto(
                id = 1,
                type = "Lithium-Ion",
                mentor = Mentor(1, "Doe, John", "John", "Doe")
            ),
            BatteryDto(
                id = 2,
                type = "Loodzuur",
                mentor = null
            )
        )))
    }

    override suspend fun assignMentor(batteryId: Int, mentorId: Int): Flow<APIResource<Unit>> = flow {
        emit(APIResource.Success(Unit))
    }

    override suspend fun getBatteriesByBoat(boatId: Int): Flow<APIResource<List<BatteryDto>>> = flow {
        emit(APIResource.Success(listOf(
            BatteryDto(
                id = 1,
                type = "Lithium-Ion",
                mentor = Mentor(1, "Doe, John", "John", "Doe")
            ),
            BatteryDto(
                id = 2,
                type = "Loodzuur",
                mentor = null
            )
        )))
    }
} 