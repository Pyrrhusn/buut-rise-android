package com.example.android_2425_gent2.ui.screens.reservations_page

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_2425_gent2.data.model.OfflineReservation
import com.example.android_2425_gent2.data.network.model.ReservationDetailsDto
import com.example.android_2425_gent2.data.repository.APIResource
import com.example.android_2425_gent2.data.repository.reservation.ReservationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class ReservationsViewModel(private val reservationRepository: ReservationRepository) : ViewModel() {
    var reservationTypeUiState by mutableStateOf(ReservationTypeUiSate(ReservationType.UPCOMING))
        private set

    private val _reservationsUiState = MutableStateFlow(ReservationsUiState(loading = true))
    val reservationsUiState: StateFlow<ReservationsUiState> = _reservationsUiState

    var selectedReservationUiState by mutableStateOf(SelectedReservationUiState(null))
        private set

    fun setReservationType(reservationType: ReservationType) {
        reservationTypeUiState = ReservationTypeUiSate(reservationType)
        loadReservationsForCurrentType()
    }

    init {
        loadReservationsForCurrentType()
    }


    fun setSelectedReservation(reservation: OfflineReservation?) {
        selectedReservationUiState = SelectedReservationUiState(reservation)
        if (reservation != null) {
            loadReservationDetails(reservation.id)
        }
    }

    private fun loadReservationsForCurrentType() {
        viewModelScope.launch {
            val getPast = when (reservationTypeUiState.reservationType) {
                ReservationType.UPCOMING -> false
                ReservationType.OLD -> true
                ReservationType.CANCELED -> false
            }

            reservationRepository.getReservations(getPast = getPast).collect { apiResource ->
                when (apiResource) {
                    is APIResource.Loading -> {
                        _reservationsUiState.value = ReservationsUiState(loading = true)
                    }

                    is APIResource.Success -> {
                        val reservations = apiResource.data
                        if (reservations != null) {
                            // Filter de reserveringen op basis van het type
                            val filteredReservations = when (reservationTypeUiState.reservationType) {
                                ReservationType.CANCELED -> reservations.filter { it.isDeleted }
                                else -> reservations.filter { !it.isDeleted }
                            }

                            _reservationsUiState.value = ReservationsUiState(
                                reservations = filteredReservations,
                                loading = false
                            )
                        } else {
                            _reservationsUiState.value = ReservationsUiState(
                                hasError = true,
                                errorMessage = "No data available"
                            )
                        }
                    }

                    is APIResource.Error -> {
                        _reservationsUiState.value = ReservationsUiState(
                            hasError = true,
                            errorMessage = apiResource.message,
                            loading = false
                        )
                    }
                }
            }
        }
    }
    private val _cancelReservationState = MutableStateFlow<APIResource<Unit>?>(null)
    val cancelReservationState: StateFlow<APIResource<Unit>?> = _cancelReservationState

    fun cancelReservation(reservationId: Int) {
        viewModelScope.launch {
            reservationRepository.cancelReservation(reservationId).collect { result ->
                when (result) {
                    is APIResource.Success -> {
                        // Eerst bottom sheet sluiten
                        setSelectedReservation(null)

                        // Herlaad de huidige reservatietype (zorgt voor het gewenste gedrag)
                        setReservationType(reservationTypeUiState.reservationType)
                    }
                    is APIResource.Error -> {
                        _reservationsUiState.value = ReservationsUiState(
                            hasError = true,
                            errorMessage = result.message
                        )
                    }
                    is APIResource.Loading -> {
                        // Optioneel: loading state tonen
                    }
                }
            }
        }
    }

    private fun loadReservationDetails(reservationId: Int) {
        println("Loading details for reservation: $reservationId")
        viewModelScope.launch {
            try {
                selectedReservationUiState = selectedReservationUiState.copy(isLoadingDetails = true)
                reservationRepository.getReservationDetails(reservationId).collect { result ->
                    println("Received details result: $result")
                    selectedReservationUiState = when (result) {
                        is APIResource.Loading -> {
                            println("Details loading state")
                            selectedReservationUiState.copy(isLoadingDetails = true)
                        }

                        is APIResource.Success -> {
                            println("Details success: ${result.data}")
                            result.data?.let { details ->
                                if (details.currentBatteryUserName == "Unknown") {
                                    selectedReservationUiState.copy(
                                        details = null,
                                        error = "Details temporarily unavailable",
                                        isLoadingDetails = false
                                    )
                                } else {
                                    selectedReservationUiState.copy(
                                        details = details,
                                        isLoadingDetails = false,
                                        error = null
                                    )
                                }
                            } ?: selectedReservationUiState.copy(
                                error = "No details available",
                                isLoadingDetails = false
                            )
                        }

                        is APIResource.Error -> {
                            println("Details error: ${result.message}")
                            selectedReservationUiState.copy(
                                error = result.message,
                                isLoadingDetails = false
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                selectedReservationUiState = selectedReservationUiState.copy(
                    error = "Failed to load details",
                    isLoadingDetails = false
                )
            }
        }
    }
}
    data class ReservationsUiState(
        val reservations: List<OfflineReservation> = emptyList(),
        val loading: Boolean = false,
        val hasError: Boolean = false,
        val errorMessage: String? = null
    )

    data class SelectedReservationUiState(
        val selectedReservation: OfflineReservation?,
        val details: ReservationDetailsDto? = null,
        val isLoadingDetails: Boolean = false,
        val error: String? = null
    )

    data class ReservationTypeUiSate(
        val reservationType: ReservationType
    )

