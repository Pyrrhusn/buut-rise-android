package com.example.android_2425_gent2.data.repository.boat

import com.example.android_2425_gent2.data.network.boat.BoatDto
import com.example.android_2425_gent2.data.repository.APIResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

open class TestBoatRepository : BoatRepository {
    private var isLoading = false
    private var errorMessage: String? = null
    
    fun setLoading(loading: Boolean) {
        isLoading = loading
        errorMessage = null
    }
    
    fun setError(message: String) {
        errorMessage = message
        isLoading = false
    }

    override suspend fun getBoats(): Flow<APIResource<List<BoatDto>>> = flow {
        if (isLoading) {
            emit(APIResource.Loading())
            return@flow
        }
        
        errorMessage?.let {
            emit(APIResource.Error(it))
            return@flow
        }
        
        emit(APIResource.Success(listOf(
            BoatDto(1, "Test Boat 1", "Limba", true),
            BoatDto(2, "Test Boat 2", "Leith", true),
            BoatDto(3, "Test Boat 3", "Lubeck", true)
        )))
    }
} 