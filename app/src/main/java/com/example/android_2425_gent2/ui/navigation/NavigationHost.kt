package com.example.android_2425_gent2.ui.navigation

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.android_2425_gent2.ui.AppViewModelProvider
import com.example.android_2425_gent2.ui.screens.CalendarPage
import com.example.android_2425_gent2.ui.screens.HomePage
import com.example.android_2425_gent2.ui.screens.notification_page.NotificationScreen
import com.example.android_2425_gent2.ui.screens.notification_page.NotificationViewModel

@Composable
fun NavigationHost(
    navController: NavHostController,
    windowSize: WindowWidthSizeClass,
    modifier: Modifier,
    logout: () -> Unit,
    notificationViewModel: NotificationViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Calendar.route,
        modifier = modifier
    ) {
        composable(route = BottomNavItem.Calendar.route) {
            CalendarPage()
        }
        composable(route = BottomNavItem.Notifications.route) {
            NotificationScreen(windowSize = windowSize)
        }
        profileNavigation(navController, modifier, logout)
    }
}