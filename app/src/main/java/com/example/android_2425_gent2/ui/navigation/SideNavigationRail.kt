package com.example.android_2425_gent2.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.android_2425_gent2.R
import com.example.android_2425_gent2.ui.AppViewModelProvider
import com.example.android_2425_gent2.ui.screens.notification_page.NotificationViewModel
import java.util.Locale

@Composable
fun SideNavigationRail(
    navController: NavHostController,
    notificationViewModel: NotificationViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .width(80.dp)
            .shadow(
                elevation = 8.dp,
                spotColor = Color.Black.copy(alpha = 0.25f),
                ambientColor = Color.Black.copy(alpha = 0.25f),
                clip = false
            )
    ) {
        NavigationRail(
            modifier = modifier.padding(vertical = 8.dp),
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            val unreadCount by notificationViewModel.unreadCount.collectAsState()
            val primaryColor = colorResource(id = R.color.primary)

            Spacer(modifier.height(16.dp))

            BottomNavItem.entries.forEach { item ->
                if (item == BottomNavItem.Notifications) {
                    item.badgeCount = unreadCount
                }
                NavigationRailItem(
                    selected = currentRoute == item.route,
                    onClick = { navController.navigate(item.route) },
                    colors = NavigationRailItemDefaults.colors(
                        selectedIconColor = primaryColor,
                        selectedTextColor = primaryColor,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color.Transparent,
                    ),
                    icon = {
                        BadgedBox(
                            badge = {
                                if (item.badgeCount > 0) {
                                    Badge(
                                        containerColor = primaryColor
                                    ) {
                                        Text(
                                            text = item.badgeCount.toString(),
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null
                            )
                        }
                    },
                    label = {
                        Text(
                            text = stringResource(item.labelStringResourceId)
                                .replaceFirstChar {
                                    if (it.isLowerCase()) it.titlecase(Locale.getDefault())
                                    else it.toString()
                                },
                            fontWeight = if (currentRoute == item.route) FontWeight.SemiBold
                            else FontWeight.Normal
                        )
                    }
                )
            }
        }
    }
}