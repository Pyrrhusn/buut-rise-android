package com.example.android_2425_gent2.data.network // Adjust the package name accordingly

import com.example.android_2425_gent2.data.network.model.RemoteReservation
import com.example.android_2425_gent2.data.remote.model.TimeSlot
import com.example.android_2425_gent2.data.remote.model.TimeSlotResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("api/Reservation/user/2")
    suspend fun getReservations(): List<RemoteReservation>

    @GET("/api/TimeSlot/range")
    suspend fun getTimeSlots(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): TimeSlotResponse


    @GET("/api/TimeSlot/{year}/{month}/{day}")
    suspend fun getTimeSlotsForDay(
        @Path("year") year: Int,
        @Path("month") month: Int,
        @Path("day") day: Int
    ): List<TimeSlot>
}


