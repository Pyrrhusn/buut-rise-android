package com.example.android_2425_gent2.ui.screens.app_page

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.android_2425_gent2.data.repository.auth.IAuthRepo

class AppViewModel(
    private val authRepo: IAuthRepo
) : ViewModel() {
    var appState by mutableStateOf(AppState(isLoggedIn = authRepo.isLoggedIn()))
        private set

    fun verifyLoginState() {
        appState = appState.copy(isLoggedIn = authRepo.isLoggedIn())
    }
}