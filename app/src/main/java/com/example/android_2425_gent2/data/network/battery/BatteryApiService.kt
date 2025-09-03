package com.example.android_2425_gent2.data.network.battery

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.Path

interface BatteryApiService {

    suspend fun getAllBatteries(): Response<List<BatteryDto>>
    
    @GET("/api/Battery/boat/{boatId}")
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    suspend fun getBatteriesByBoat(@Path("boatId") boatId: Int): Response<List<BatteryDto>>
    
    @PUT("/api/Battery/{id}")
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    suspend fun updateBattery(
        @Path("id") id: Int,
        @Body request: UpdateBatteryRequest
    ): Response<Unit>
}


data class UpdateBatteryRequest(
    val type: String,
    val mentorId: Int?,
    val newBattery: Boolean
) 