package com.example.android_2425_gent2.ui.screens.profile_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_2425_gent2.data.network.model.UserDetailsDto
import com.example.android_2425_gent2.data.repository.APIResource
import com.example.android_2425_gent2.data.repository.user.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UserDetailsUiState(
    val userDetails: UserDetailsDto? = null,
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val isUpdateSuccess: Boolean = false
)

class UserDetailsViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UserDetailsUiState())
    val uiState: StateFlow<UserDetailsUiState> = _uiState.asStateFlow()

    fun fetchUserDetails(userId: String) {
        viewModelScope.launch {
            userRepository.getUserDetails(userId).collect { result ->
                when (result) {
                    is APIResource.Loading -> {
                        _uiState.update { it.copy(isLoading = true, errorMessage = "") }
                    }
                    is APIResource.Success -> {
                        _uiState.update {
                            it.copy(
                                userDetails = result.data,
                                isLoading = false,
                                errorMessage = ""
                            )
                        }
                    }
                    is APIResource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Unknown error occurred"
                            )
                        }
                    }
                }
            }
        }
    }

    fun approveUser(userId: Int) {
        viewModelScope.launch {
            userRepository.updateUserRole(userId, "Member").collect { result ->
                when (result) {
                    is APIResource.Loading -> {
                        _uiState.update { it.copy(isLoading = true, errorMessage = "") }
                    }
                    is APIResource.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = "",
                                isUpdateSuccess = true
                            )
                        }
                    }
                    is APIResource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Unknown error occurred"
                            )
                        }
                    }
                }
            }
        }
    }
}