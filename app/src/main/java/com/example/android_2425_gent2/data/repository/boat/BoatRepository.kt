package com.example.android_2425_gent2.data.repository.boat

import com.example.android_2425_gent2.data.network.boat.BoatDto
import com.example.android_2425_gent2.data.repository.APIResource
import kotlinx.coroutines.flow.Flow

interface BoatRepository {
    suspend fun getBoats(): Flow<APIResource<List<BoatDto>>>
} 