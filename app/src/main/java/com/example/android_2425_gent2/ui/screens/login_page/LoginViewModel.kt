package com.example.android_2425_gent2.ui.screens.login_page

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth0.android.result.Credentials
import com.example.android_2425_gent2.BuildConfig
import com.example.android_2425_gent2.data.repository.APIResource
import com.example.android_2425_gent2.data.repository.auth.IAuthRepo
import kotlinx.coroutines.launch

class LoginViewModel(
    val login : (Credentials)->Unit,
    val authRepo: IAuthRepo
) : ViewModel() {

    private val _openUrlEvent = mutableStateOf<String?>(null)
    val openUrlEvent = _openUrlEvent
    private val emailPattern = """^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$"""
    private val registerUrl = BuildConfig.REGISTRATION_URL

    var credentialsState by mutableStateOf(CredentialsState("", ""))
        private set

    var uiState by mutableStateOf(UiState())
        private set

    fun setEmail(email: String) {
        credentialsState = credentialsState.copy(
            email = email,
            emailTouched = true
        )
    }

    fun setPassword(password: String) {
        credentialsState = credentialsState.copy(
            password = password,
            passwordTouched = true
        )
    }

    fun onAnyInputChanged() {
        setDisableLogin(!validateCredentials())
    }

    private fun setLoading(isLoading: Boolean) {
        uiState = uiState.copy(
            isLoading = isLoading
        )
    }

    fun setError(error: String?) {
        uiState = uiState.copy(
            error = error
        )
    }

    private fun setDisableLogin(disable: Boolean) {
        uiState = UiState(uiState.isLoading, uiState.error, disable)
    }

    private fun validateCredentials(): Boolean {
        return when {
            credentialsState.emailTouched && credentialsState.email.isEmpty() -> {
                setError("Email is required")
                false
            }
            credentialsState.emailTouched && !credentialsState.email.matches(emailPattern.toRegex()) -> {
                setError("Invalid email format")
                false
            }
            credentialsState.passwordTouched && credentialsState.password.isEmpty() -> {
                setError("Password is required")
                false
            }
            credentialsState.passwordTouched && credentialsState.password.length < 8 -> {
                setError("Password must be at least 8 characters")
                false
            }
            credentialsState.passwordTouched && credentialsState.password.length >= 72 -> {
                setError("Password must be less than or equal to 72 characters")
                false
            }
            else -> {
                setError(null)
                credentialsState.email.isNotEmpty() && credentialsState.password.isNotEmpty() &&
                        credentialsState.email.matches(emailPattern.toRegex())
            }
        }
    }

    fun handleLogin() {
        viewModelScope.launch {
            setLoading(true)
            authRepo.login(userName = credentialsState.email, password = credentialsState.password)
                .collect { response ->
                when (response) {
                    is APIResource.Loading -> {
                        setLoading(true)
                    }
                    is APIResource.Success -> {
                        setLoading(false)

                        response.data?.let { login(it) } ?: setError("Invalid credentials")
                    }
                    is APIResource.Error -> {
                        setLoading(false)
                        setError(response.message ?: "An error occurred")
                    }
                }
            }
        }
    }

    fun handleRegister() {
        _openUrlEvent.value = registerUrl
    }

    fun onUrlOpened() {
        _openUrlEvent.value = null
    }
}

data class CredentialsState(
    val email: String,
    val password: String,
    val emailTouched: Boolean = false,
    val passwordTouched: Boolean = false
)

data class UiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val disableLogin: Boolean = true
)