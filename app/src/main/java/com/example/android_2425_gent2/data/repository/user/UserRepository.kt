package com.example.android_2425_gent2.data.repository.user

import com.example.android_2425_gent2.data.model.UserSurface
import com.example.android_2425_gent2.data.network.model.UserDetailsDto
import com.example.android_2425_gent2.data.network.users.UserNameDto
import com.example.android_2425_gent2.data.repository.APIResource
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun getUsers() : Flow<APIResource<List<UserSurface>>>
    suspend fun getUserDetails(userId: String): Flow<APIResource<UserDetailsDto>>
    suspend fun getUserDetails(): Flow<APIResource<UserDetailsDto>>
    suspend fun updateUserRole(userId: Int, role: String): Flow<APIResource<Unit>>
    suspend fun getUserNames(): Flow<APIResource<List<UserNameDto>>>
  
}