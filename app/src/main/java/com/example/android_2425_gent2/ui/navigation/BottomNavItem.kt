package com.example.android_2425_gent2.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.android_2425_gent2.R

enum class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val labelStringResourceId: Int,
    var badgeCount: Int
) {
    Calendar("calendar", Icons.Default.DateRange, R.string.calendar, 0),
    Notifications("notifications", Icons.Default.Notifications, R.string.notifications, 0),
    Profile("profile", Icons.Default.Person, R.string.profile, 0);

}