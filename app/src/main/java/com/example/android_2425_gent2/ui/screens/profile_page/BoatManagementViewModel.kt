package com.example.android_2425_gent2.ui.screens.profile_page

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_2425_gent2.data.network.battery.BatteryDto
import com.example.android_2425_gent2.data.network.boat.BoatDto
import com.example.android_2425_gent2.data.network.users.UserNameDto
import com.example.android_2425_gent2.data.repository.APIResource
import com.example.android_2425_gent2.data.repository.battery.BatteryRepository
import com.example.android_2425_gent2.data.repository.boat.BoatRepository
import com.example.android_2425_gent2.data.repository.user.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BoatManagementUiState(
    val boats: List<BoatDto> = emptyList(),
    val batteries: List<BatteryDto> = emptyList(),
    val users: List<UserNameDto> = emptyList(),
    val selectedBoatId: Int? = null,
    val selectedBatteryId: Int? = null,
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val successMessage: String = ""
)

class BoatManagementViewModel(
    private val batteryRepository: BatteryRepository,
    private val boatRepository: BoatRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(BoatManagementUiState())
    val uiState: StateFlow<BoatManagementUiState> = _uiState.asStateFlow()

    init {
        loadBoats()
        loadUsers()
    }

    private fun loadUsers() {
        viewModelScope.launch {
            userRepository.getUserNames().collect { result ->
                when (result) {
                    is APIResource.Success -> {
                        _uiState.update { it.copy(users = result.data ?: emptyList()) }
                    }
                    is APIResource.Error -> {
                        _uiState.update { it.copy(errorMessage = result.message ?: "Failed to load users") }
                    }
                    is APIResource.Loading -> {
                        // Handle loading state if needed
                    }
                }
            }
        }
    }

    private fun loadBoats() {
        viewModelScope.launch {
            boatRepository.getBoats().collect { result ->
                when (result) {
                    is APIResource.Loading -> {
                        _uiState.update { it.copy(isLoading = true, errorMessage = "") }
                    }
                    is APIResource.Success -> {
                        _uiState.update {
                            it.copy(
                                boats = result.data ?: emptyList(),
                                isLoading = false,
                                errorMessage = ""
                            )
                        }
                    }
                    is APIResource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Failed to load boats"
                            )
                        }
                    }
                }
            }
        }
    }

    fun selectBoat(boatId: Int) {
        _uiState.update { it.copy(selectedBoatId = boatId) }
        loadBatteriesForBoat(boatId)
    }

    private fun loadBatteriesForBoat(boatId: Int) {
        viewModelScope.launch {
            batteryRepository.getBatteriesByBoat(boatId).collect { result ->
                when (result) {
                    is APIResource.Loading -> {
                        _uiState.update { it.copy(isLoading = true, errorMessage = "") }
                    }
                    is APIResource.Success -> {
                        _uiState.update {
                            it.copy(
                                batteries = result.data ?: emptyList(),
                                isLoading = false,
                                errorMessage = ""
                            )
                        }
                    }
                    is APIResource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Failed to load batteries"
                            )
                        }
                    }
                }
            }
        }
    }

    fun assignMentor(batteryId: Int, mentorId: Int) {
        viewModelScope.launch {
            batteryRepository.assignMentor(batteryId, mentorId).collect { result ->
                when (result) {
                    is APIResource.Loading -> {
                        _uiState.update { it.copy(isLoading = true, errorMessage = "") }
                    }
                    is APIResource.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = "",
                                successMessage = "Mentor successfully assigned"
                            )
                        }
                        _uiState.value.selectedBoatId?.let { boatId ->
                            loadBatteriesForBoat(boatId)
                        }
                    }
                    is APIResource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Failed to assign mentor"
                            )
                        }
                    }
                }
            }
        }
    }

    fun clearSuccessMessage() {
        _uiState.update { it.copy(successMessage = "") }
    }
} 