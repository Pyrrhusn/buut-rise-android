package com.example.android_2425_gent2.data.repository.reservation

import com.example.android_2425_gent2.data.local.dao.OfflineReservationDao
import com.example.android_2425_gent2.data.local.entity.asExternalModel
import com.example.android_2425_gent2.data.local.entity.asReservationDetails
import com.example.android_2425_gent2.data.model.OfflineReservation
import com.example.android_2425_gent2.data.network.model.CreateRemoteReservationRequest
import com.example.android_2425_gent2.data.network.model.ReservationDetailsDto
import com.example.android_2425_gent2.data.network.model.asEntity
import com.example.android_2425_gent2.data.network.reservation.ReservationApiService
import com.example.android_2425_gent2.data.repository.APIResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class OfflineFirstReservationRepository(
    private val reservationDao: OfflineReservationDao,
    private val remoteApiService: ReservationApiService
) : ReservationRepository {

    private suspend fun fetchAndStoreAllReservations(pageSize: Int = 20) {
        // Fetch future reservations
        fetchReservationsForType(getPast = false, pageSize = pageSize)
        // Fetch past reservations
        fetchReservationsForType(getPast = true, pageSize = pageSize)
    }

    private suspend fun fetchReservationsForType(getPast: Boolean, pageSize: Int) {
        var currentCursor: Int? = null
        var hasMorePages = true

        while (hasMorePages) {
            try {
                val queryParams = buildMap<String, Any> {
                    currentCursor?.let { put("cursor", it) }
                    put("getPast", getPast)
                    put("pageSize", pageSize)
                }

                val response = remoteApiService.getReservationPage(queryParams)

                withContext(Dispatchers.IO) {
                    reservationDao.insert(response.data.map { it.asEntity() })
                }

                currentCursor = response.nextId
                hasMorePages = response.nextId != null && response.data.isNotEmpty()

                delay(100)
            } catch (e: Exception) {
                // If there's an error, break the loop
                break
            }
        }
    }

    override suspend fun getReservations(
        getPast: Boolean,
    ): Flow<APIResource<List<OfflineReservation>>> = flow {
        emit(APIResource.Loading())

        val reservationsFlow = reservationDao.getOfflineReservations(getPast = getPast)
            .distinctUntilChanged()
            .map { localReservations ->
                APIResource.Success(
                    localReservations.map { it.asExternalModel() },
                )
            }

        try {
            fetchAndStoreAllReservations()
        } catch (e: Exception) {
            // On error, we emit error only if local database is empty
            val localData = reservationDao.getOfflineReservations(getPast = getPast).first()
            if (localData.isEmpty()) {
                emit(APIResource.Error("No reservations found"))
                return@flow
            }
        }

        reservationsFlow.collect { emission ->
            emit(emission)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun insertReservation(
        createRemoteReservationRequest: CreateRemoteReservationRequest
    ): Flow<APIResource<Int>> = flow {
        emit(APIResource.Loading())

        val result = withContext(Dispatchers.IO) {
            try {
                val response = remoteApiService.createReservation(createRemoteReservationRequest)
                // Fetch all reservations after successful insertion
                fetchAndStoreAllReservations()
                APIResource.Success(response)
            } catch (e: Exception) {
                APIResource.Error("Failed to create reservation: ${e.message}")
            }
        }

        emit(result)
    }.flowOn(Dispatchers.IO)

     override suspend fun getReservationDetails(reservationId: Int): Flow<APIResource<ReservationDetailsDto>> = flow {
        emit(APIResource.Loading())
        
        try {
            // Try remote first
            val response = remoteApiService.getReservationDetails(reservationId)
            if (response.isSuccessful && response.body() != null) {
                val details = response.body()!!
                
                // Update local database
                withContext(Dispatchers.IO) {
                    val localReservation = reservationDao.getOfflineReservationById(reservationId).first()
                    val updatedEntity = localReservation?.copy(
                        isDeleted = details.isDeleted,
                        mentorName = details.mentorName,
                        batteryId = details.batteryId,
                        currentBatteryUserName = details.currentBatteryUserName,
                        currentBatteryUserId = details.currentBatteryUserId,
                        currentHolderPhoneNumber = details.currentHolderPhoneNumber,
                        currentHolderEmail = details.currentHolderEmail,
                        currentHolderStreet = details.currentHolderStreet,
                        currentHolderNumber = details.currentHolderNumber,
                        currentHolderCity = details.currentHolderCity,
                        currentHolderPostalCode = details.currentHolderPostalCode
                    )
                    if (updatedEntity != null) {
                        reservationDao.update(updatedEntity)
                    }
                }
                
                emit(APIResource.Success(details))
            } else {
                // If remote fails, try local
                val localReservation = reservationDao.getOfflineReservationById(reservationId).first()
                if (localReservation != null) {
                    emit(APIResource.Success(localReservation.asReservationDetails()))
                } else {
                    emit(APIResource.Error("Failed to fetch reservation details"))
                }
            }
        } catch (e: Exception) {
            // If exception occurs, try local
            val localReservation = reservationDao.getOfflineReservationById(reservationId).first()
            if (localReservation != null) {
                emit(APIResource.Success(localReservation.asReservationDetails()))
            } else {
                emit(APIResource.Error("Failed to fetch reservation details: ${e.message}"))
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun cancelReservation(reservationId: Int): Flow<APIResource<Unit>> = flow {
        emit(APIResource.Loading())

        try {
            val response = remoteApiService.cancelReservation(reservationId)

            if (response.isSuccessful) {
                // Update lokale database
                withContext(Dispatchers.IO) {
                    val existingReservation = reservationDao.getOfflineReservationByIdForCancel(reservationId)
                    existingReservation?.let {
                        reservationDao.insert(it.copy(isDeleted = true))
                    }
                }
                emit(APIResource.Success(Unit))
            } else {
                emit(APIResource.Error("Failed to cancel reservation"))
            }
        } catch (e: Exception) {
            emit(APIResource.Error("Failed to cancel reservation: ${e.message}"))
        }
    }.flowOn(Dispatchers.IO)
}

