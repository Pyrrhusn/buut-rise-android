package com.example.android_2425_gent2.ui.navigation

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.android_2425_gent2.ui.screens.notification_page.NotificationScreen

object NotificationNavigation {
    const val NOTIFICATION_ROUTE = "notifications"
}

fun NavGraphBuilder.notificationNavigation(
    navController: NavController,
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier
) {
    composable(route = BottomNavItem.Notifications.route) {
        NotificationScreen(
            windowSize = windowSize,
            modifier = modifier
        )
    }
}