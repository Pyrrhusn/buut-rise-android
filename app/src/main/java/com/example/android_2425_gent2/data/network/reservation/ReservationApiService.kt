package com.example.android_2425_gent2.data.network.reservation

import com.example.android_2425_gent2.data.network.model.CreateRemoteReservationRequest
import com.example.android_2425_gent2.data.network.model.ReservationResponse
import retrofit2.Response
import com.example.android_2425_gent2.data.network.model.ReservationDetailsDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.QueryMap

import retrofit2.http.Headers


interface ReservationApiService {
    @POST("/api/Reservation")
    suspend fun createReservation(
        @Body reservation: CreateRemoteReservationRequest
    ): Int

    @GET("/api/Reservation/me")
    suspend fun getReservationPage(
        @QueryMap queryParams: Map<String, @JvmSuppressWildcards Any>
    ): ReservationResponse

    @PATCH("/api/Reservation/cancel/{id}")
    suspend fun cancelReservation(
        @Path("id") reservationId: Int
    ): Response<Unit>

    @GET("/api/Reservation/{id}")
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    suspend fun getReservationDetails(
        @Path("id") reservationId: Int
    ): Response<ReservationDetailsDto>
}