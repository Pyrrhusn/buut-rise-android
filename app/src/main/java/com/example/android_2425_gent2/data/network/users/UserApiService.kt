package com.example.android_2425_gent2.data.network.users

import com.example.android_2425_gent2.data.network.model.PaginatedResponse
import com.example.android_2425_gent2.data.network.model.UpdateUserRoleRequest
import com.example.android_2425_gent2.data.network.model.UserDetailsDto
import com.example.android_2425_gent2.data.network.model.UserSurfaceInfoDto
import retrofit2.http.Body
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface UserApiService {
    @GET("/api/User?role=Guest")
    suspend fun getGuests(): PaginatedResponse<UserSurfaceInfoDto>

    @GET("/api/User/{id}")
    suspend fun getUserDetails(@Path("id") id: String): UserDetailsDto
    @GET("/api/User/profile")
    suspend fun getUserDetails(): UserDetailsDto

    @POST("/api/User/role")
    suspend fun updateUserRole(@Body request: UpdateUserRoleRequest)

    @GET("/api/User/names")
    suspend fun getUserNames(): Response<List<UserNameDto>>
}