package com.example.android_2425_gent2.data.repository.battery

import com.example.android_2425_gent2.data.network.battery.BatteryApiService
import com.example.android_2425_gent2.data.network.battery.BatteryDto
import com.example.android_2425_gent2.data.network.battery.UpdateBatteryRequest
import com.example.android_2425_gent2.data.repository.APIResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class NetworkBatteryRepository(
    private val batteryApiService: BatteryApiService
) : BatteryRepository {

    override suspend fun getAllBatteries(): Flow<APIResource<List<BatteryDto>>> = flow {
        emit(APIResource.Loading())
        
        val result = withContext(Dispatchers.IO) {
            try {
                println("Making API call to fetch batteries...")
                val response = batteryApiService.getAllBatteries()
                
                // Log the raw response string
                val rawResponse = response.raw()
                println("Raw Response: $rawResponse")
                
                // Try to get the response body as string for debugging
                val responseBodyString = response.errorBody()?.string() 
                    ?: response.body()?.toString() 
                    ?: "No response body"
                println("Response Body as String: $responseBodyString")
                
                if (!response.isSuccessful) {
                    return@withContext APIResource.Error(
                        "API call failed with code ${response.code()}: $responseBodyString"
                    )
                }

                // If we got here, try to parse the response
                val body = response.body()
                if (body != null) {
                    APIResource.Success(body)
                } else {
                    APIResource.Error("Empty response body. Raw response: $responseBodyString")
                }
            } catch (e: Exception) {
                println("Exception occurred while fetching batteries: ${e.message}")
                println("Exception class: ${e.javaClass.name}")
                println("Exception stack trace:")
                e.printStackTrace()
                
                // Create a more detailed error message
                val errorMessage = when (e) {
                    is IllegalStateException -> {
                        "Invalid response format from server. Expected a list of batteries but got something else. Error: ${e.message}"
                    }
                    else -> "Failed to fetch batteries: ${e.message}"
                }
                APIResource.Error(errorMessage)
            }
        }
        
        emit(result)
    }.flowOn(Dispatchers.IO)

    override suspend fun assignMentor(batteryId: Int, mentorId: Int): Flow<APIResource<Unit>> = flow {
        emit(APIResource.Loading())
        
        val result = withContext(Dispatchers.IO) {
            try {
                println("Making API call to assign mentor (batteryId: $batteryId, mentorId: $mentorId)...")
                val request = UpdateBatteryRequest(
                    type = "Lithium-Ion",
                    mentorId = mentorId,
                    newBattery = false
                )
                val response = batteryApiService.updateBattery(batteryId, request)
                
                println("Response raw: ${response.raw()}")
                println("Response headers: ${response.headers()}")
                println("Response code: ${response.code()}")
                
                if (!response.isSuccessful) {
                    val errorBody = response.errorBody()?.string()
                    println("Error body: $errorBody")
                    return@withContext APIResource.Error("Failed to assign mentor: $errorBody")
                }
                
                APIResource.Success(Unit)
            } catch (e: Exception) {
                println("Exception occurred while assigning mentor: ${e.message}")
                println("Exception stack trace:")
                e.printStackTrace()
                APIResource.Error("Failed to assign mentor: ${e.message}")
            }
        }
        
        emit(result)
    }.flowOn(Dispatchers.IO)

    override suspend fun getBatteriesByBoat(boatId: Int): Flow<APIResource<List<BatteryDto>>> = flow {
        emit(APIResource.Loading())
        
        val result = withContext(Dispatchers.IO) {
            try {
                println("Making API call to fetch batteries for boat $boatId...")
                val response = batteryApiService.getBatteriesByBoat(boatId)
                
                // Log the raw response for debugging
                val rawResponse = response.raw()
                println("Raw Response: $rawResponse")
                
                // Try to get the response body as string for debugging
                val responseBodyString = response.errorBody()?.string() 
                    ?: response.body()?.toString() 
                    ?: "No response body"
                println("Response Body as String: $responseBodyString")
                
                if (!response.isSuccessful) {
                    return@withContext APIResource.Error(
                        "API call failed with code ${response.code()}: $responseBodyString"
                    )
                }

                // If we got here, try to parse the response
                val body = response.body()
                if (body != null) {
                    APIResource.Success(body)
                } else {
                    APIResource.Error("Empty response body. Raw response: $responseBodyString")
                }
            } catch (e: Exception) {
                println("Exception occurred while fetching batteries for boat $boatId: ${e.message}")
                println("Exception class: ${e.javaClass.name}")
                println("Exception stack trace:")
                e.printStackTrace()
                
                val errorMessage = when (e) {
                    is IllegalStateException -> {
                        "Invalid response format from server. Expected a list of batteries but got something else. Error: ${e.message}"
                    }
                    else -> "Failed to fetch batteries for boat $boatId: ${e.message}"
                }
                APIResource.Error(errorMessage)
            }
        }
        
        emit(result)
    }.flowOn(Dispatchers.IO)
} 