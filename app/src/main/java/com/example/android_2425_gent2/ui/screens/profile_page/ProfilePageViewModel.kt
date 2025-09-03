package com.example.android_2425_gent2.ui.screens.profile_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_2425_gent2.data.model.UserRole
import com.example.android_2425_gent2.data.network.model.AddressDto
import com.example.android_2425_gent2.data.network.model.UserDetailsDto
import com.example.android_2425_gent2.data.repository.APIResource
import com.example.android_2425_gent2.data.repository.auth.IAuthRepo
import com.example.android_2425_gent2.data.repository.user.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfilePageUiState(
    val isAdmin: Boolean = false,
    val user: UserDetailsDto = UserDetailsDto("firstName", "email", "phoneNumber", AddressDto("street", "number", "city", "postalCode", "country"), "familyName", 0 ),
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)

class ProfilePageViewModel(
    private val authRepo: IAuthRepo,
    private val userRepository: UserRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(ProfilePageUiState())
    val uiState: StateFlow<ProfilePageUiState> = _uiState.asStateFlow()

    fun handleLogout(onLogoutComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                authRepo.logout()
                onLogoutComplete()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "Logout failed: ${e.message}")
                }
            }
        }
    }


    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isAdmin = authRepo.hasRole(UserRole.Administrator)
                )
            }
            userRepository.getUserDetails().collect { result ->
                when (result) {

                    is APIResource.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true,
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

                    is APIResource.Success -> {
                        _uiState.update {
                            it.copy(
                                user = result.data ?: UserDetailsDto("firstName", "email", "phoneNumber", AddressDto("street", "number", "city", "postalCode", "country"), "familyName", 0 ),
                                isLoading = false,
                                errorMessage = ""

                            )
                        }
                    }
                }
            }
        }
    }


}