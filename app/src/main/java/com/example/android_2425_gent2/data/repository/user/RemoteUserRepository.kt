package com.example.android_2425_gent2.data.repository.user

import com.example.android_2425_gent2.data.model.UserSurface
import com.example.android_2425_gent2.data.model.toDomain
import com.example.android_2425_gent2.data.network.model.UpdateUserRoleRequest
import com.example.android_2425_gent2.data.network.model.UserDetailsDto
import com.example.android_2425_gent2.data.network.users.UserApiService
import com.example.android_2425_gent2.data.network.users.UserNameDto
import com.example.android_2425_gent2.data.repository.APIResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class RemoteUserRepository (
    private val remoteUserRepository: UserApiService

) : UserRepository {

    override suspend fun getUsers(): Flow<APIResource<List<UserSurface>>> = flow {

        emit(APIResource.Loading())

        val result = withContext(Dispatchers.IO) {
            try {
                println("trying to get users")
                val response = remoteUserRepository.getGuests().items.map { it.toDomain() }
                APIResource.Success(response)
            } catch (e: Exception) {
                APIResource.Error("Failed to fetch user surface info: ${e.message}")
            }
        }

        // Emit the final result
        emit(result)
    }.flowOn(Dispatchers.IO)

    override suspend fun getUserDetails(userId: String): Flow<APIResource<UserDetailsDto>> = flow {
        try {
            emit(APIResource.Loading())
            val response = remoteUserRepository.getUserDetails(userId)
            emit(APIResource.Success(response))
        } catch (e: Exception) {
            emit(APIResource.Error(message = "An unexpected error occurred"))
        }
    }

    override suspend fun getUserDetails(): Flow<APIResource<UserDetailsDto>> = flow {
        try {
            emit(APIResource.Loading())
            val response = remoteUserRepository.getUserDetails()
            emit(APIResource.Success(response))
        } catch (e: Exception) {
            emit(APIResource.Error(message = "An unexpected error occurred"))
        }
    }

    override suspend fun updateUserRole(userId: Int, role: String): Flow<APIResource<Unit>> = flow {
        try {
            emit(APIResource.Loading())
            remoteUserRepository.updateUserRole(UpdateUserRoleRequest(userId, role))
            emit(APIResource.Success(Unit))
        } catch (e: Exception) {
            emit(APIResource.Error(message = "An unexpected error occurred"))
        }
    }

    override suspend fun getUserNames(): Flow<APIResource<List<UserNameDto>>> = flow {
        emit(APIResource.Loading())
        
        val result = withContext(Dispatchers.IO) {
            try {
                val response = remoteUserRepository.getUserNames()
                if (!response.isSuccessful) {
                    return@withContext APIResource.Error("Failed to fetch user names: ${response.code()}")
                }
                
                val users = response.body()
                if (users != null) {
                    APIResource.Success(users)
                } else {
                    APIResource.Error("Empty response body")
                }
            } catch (e: Exception) {
                APIResource.Error("Failed to fetch user names: ${e.message}")
            }
        }
        
        emit(result)
    }.flowOn(Dispatchers.IO)
}