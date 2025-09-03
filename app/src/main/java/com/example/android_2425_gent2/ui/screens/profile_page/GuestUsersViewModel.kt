package com.example.android_2425_gent2.ui.screens.profile_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_2425_gent2.data.model.UserSurface
import com.example.android_2425_gent2.data.repository.APIResource
import com.example.android_2425_gent2.data.repository.user.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GuestUsersUiState(
    val users: List<UserSurface> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)

class GuestUsersViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(GuestUsersUiState())
    val uiState: StateFlow<GuestUsersUiState> = _uiState.asStateFlow()

    fun fetchUsers() {
        viewModelScope.launch {
            userRepository.getUsers().collect { result ->
                when (result) {
                    is APIResource.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true,
                                errorMessage = ""
                            )
                        }
                    }
                    is APIResource.Success -> {
                        _uiState.update {
                            it.copy(
                                users = result.data ?: emptyList(),
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
}