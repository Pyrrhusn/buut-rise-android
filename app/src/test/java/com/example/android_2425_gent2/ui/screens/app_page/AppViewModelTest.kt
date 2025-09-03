package com.example.android_2425_gent2.ui.screens.app_page

import com.example.android_2425_gent2.data.repository.auth.IAuthRepo
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AppViewModelTest {

    private lateinit var authRepo: IAuthRepo
    private lateinit var appViewModel: AppViewModel

    @Before
    fun setup() {
        authRepo = mockk()
        every { authRepo.isLoggedIn() } returns true

        appViewModel = AppViewModel(authRepo)
    }

    @Test
    fun isLoggedIn_AlreadyLoggedIn_True() {

        // Initialize the AppViewModel (already done in setup)

        assertEquals(true, appViewModel.appState.isLoggedIn)
    }

    @Test
    fun isLoggedIn_UpdateAppState_True() {

        appViewModel.verifyLoginState()

        assertEquals(true, appViewModel.appState.isLoggedIn)

        every { authRepo.isLoggedIn() } returns false

        appViewModel.verifyLoginState()

        assertEquals(false, appViewModel.appState.isLoggedIn)
    }
}