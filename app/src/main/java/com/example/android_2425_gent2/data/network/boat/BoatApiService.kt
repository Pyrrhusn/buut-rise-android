package com.example.android_2425_gent2.data.network.boat

import com.example.android_2425_gent2.data.network.boat.BoatDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface BoatApiService {
    @GET("/api/Boat")
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    suspend fun getBoats(): Response<List<BoatDto>>
} 