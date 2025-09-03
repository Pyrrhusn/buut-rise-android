package com.example.android_2425_gent2.ui.screens.app_page

import android.util.Log
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.android_2425_gent2.ui.AppViewModelProvider
import com.example.android_2425_gent2.ui.screens.MainScreen
import com.example.android_2425_gent2.ui.screens.login_page.LoginPage

@Composable
fun App(
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier
) {
    val viewModel: AppViewModel = viewModel(
        factory = AppViewModelProvider.Factory,
    )

    val appState = viewModel.appState

    if(!appState.isLoggedIn) {
        LoginPage(
            login = { credentials ->
                viewModel.verifyLoginState()
                Log.i("LOGIN", "App state login with token: " + credentials.accessToken)
            },
            modifier = modifier
        )
    } else {
        MainScreen(
            navController = rememberNavController(),
            windowSize = windowSize,
            modifier = modifier,
            logout = {
                viewModel.verifyLoginState()
                Log.i("LOGOUT", "User logged out")
            }
        )
    }
}