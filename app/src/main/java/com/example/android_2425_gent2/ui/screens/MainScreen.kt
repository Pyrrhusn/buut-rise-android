package com.example.android_2425_gent2.ui.screens
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.android_2425_gent2.ui.AppViewModelProvider
import com.example.android_2425_gent2.ui.navigation.AdaptiveNavigationBar
import com.example.android_2425_gent2.ui.navigation.BottomNavigationBar
import com.example.android_2425_gent2.ui.navigation.NavigationHost
import com.example.android_2425_gent2.ui.screens.notification_page.NotificationViewModel



@Composable
fun MainScreen(
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    logout: () -> Unit = {}
) {
    Row(modifier = modifier.testTag("MainScreen")) {
        if (windowSize == WindowWidthSizeClass.Expanded) {
            AdaptiveNavigationBar(
                navController = navController,
                windowSize = windowSize,
                modifier = modifier,
            )
        }

        Scaffold(
            modifier = if (windowSize == WindowWidthSizeClass.Expanded) {
                modifier.weight(1f)
            } else {
                modifier.fillMaxWidth()
            },
            bottomBar = {
                if (windowSize != WindowWidthSizeClass.Expanded) {
                    AdaptiveNavigationBar(
                        navController = navController,
                        windowSize = windowSize,
                        modifier = modifier,
                    )
                }
            }
        ) { innerPadding ->
            NavigationHost(
                modifier = modifier.padding(innerPadding),
                navController = navController,
                windowSize = windowSize,
                logout = logout
            )
        }
    }
}
@Preview
@Composable
fun MainScreenPreview() {
    MainScreen(
        windowSize = WindowWidthSizeClass.Compact,
        modifier = Modifier,
        navController = rememberNavController(),
        logout = {}
    )
}