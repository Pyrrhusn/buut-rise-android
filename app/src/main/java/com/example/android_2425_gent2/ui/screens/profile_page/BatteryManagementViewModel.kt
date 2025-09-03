package com.example.android_2425_gent2.ui.screens.profile_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_2425_gent2.data.network.battery.BatteryDto
import com.example.android_2425_gent2.data.network.users.UserNameDto
import com.example.android_2425_gent2.data.repository.APIResource
import com.example.android_2425_gent2.data.repository.battery.BatteryRepository
import com.example.android_2425_gent2.data.repository.user.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BatteryManagementUiState(
    val batteries: List<BatteryDto> = emptyList(),
    val users: List<UserNameDto> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val successMessage: String = ""
)

class BatteryManagementViewModel(
    private val batteryRepository: BatteryRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(BatteryManagementUiState())
    val uiState: StateFlow<BatteryManagementUiState> = _uiState.asStateFlow()

    init {
        loadBatteries()
        loadUsers()
    }

    private fun loadBatteries() {
        viewModelScope.launch {
            batteryRepository.getAllBatteries().collect { result ->
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
                        _uiState.update { it.copy(isLoading = true) }
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
                                successMessage = "Meter/Peter succesvol toegewezen"
                            )
                        }
                        loadBatteries() // Reload batteries to reflect changes
                    }
                    is APIResource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Toewijzen meter/peter mislukt"
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