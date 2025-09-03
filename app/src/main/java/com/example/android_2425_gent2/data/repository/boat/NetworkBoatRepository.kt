package com.example.android_2425_gent2.data.repository.boat

import com.example.android_2425_gent2.data.network.boat.BoatApiService
import com.example.android_2425_gent2.data.network.boat.BoatDto
import com.example.android_2425_gent2.data.repository.APIResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.Response

class NetworkBoatRepository(
    private val boatApiService: BoatApiService
) : BoatRepository {
    override suspend fun getBoats(): Flow<APIResource<List<BoatDto>>> = flow {
        emit(APIResource.Loading())
        
        val result = withContext(Dispatchers.IO) {
            try {
                val response = boatApiService.getBoats()
                if (!response.isSuccessful) {
                    return@withContext APIResource.Error<List<BoatDto>>(
                        "Failed to fetch boats: ${response.code()}"
                    )
                }
                
                val boats = response.body()
                if (boats != null) {
                    APIResource.Success(boats)
                } else {
                    APIResource.Error("No boats data received")
                }
            } catch (e: Exception) {
                println("Error fetching boats: ${e.message}")
                e.printStackTrace()
                APIResource.Error<List<BoatDto>>("Failed to fetch boats: ${e.message}")
            }
        }
        
        emit(result)
    }.flowOn(Dispatchers.IO)
} 