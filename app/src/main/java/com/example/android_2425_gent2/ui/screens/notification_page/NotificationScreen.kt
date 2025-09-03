package com.example.android_2425_gent2.ui.screens.notification_page

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.R
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android_2425_gent2.ui.AppViewModelProvider
import com.example.android_2425_gent2.ui.screens.notification_page.partials.NotificationContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    windowSize: WindowWidthSizeClass,
    viewModel: NotificationViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    val selectedNotification by viewModel.selectedNotification.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (selectedNotification != null && windowSize == WindowWidthSizeClass.Compact)
                            selectedNotification!!.title
                        else "Notificaties",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.testTag("NotificationPageTitle")
                    )
                },
                navigationIcon = {
                    when (selectedNotification != null && windowSize == WindowWidthSizeClass.Compact) {
                        true -> {
                            IconButton(
                                onClick = { viewModel.clearSelectedNotification() },
                                modifier = Modifier.testTag("notification_details_back_button")
                            ) {
                                Icon(
                                    Icons.Rounded.ArrowBack,
                                    contentDescription = "Terug"
                                )
                            }
                        }

                        false -> null
                    }
                }
            )
        }
    ) { paddingValues ->
        when (windowSize) {
            WindowWidthSizeClass.Compact -> {
                Box(modifier = modifier.padding(paddingValues)) {
                    if (selectedNotification != null) {
                        NotificationDetailsContent(
                            notification = selectedNotification!!,
                            modifier = modifier
                        )
                    } else {
                        NotificationPageContent(
                            notificationsUiState = viewModel.notificationsUiState.collectAsState().value,
                            onNotificationClick = { notification ->
                                viewModel.selectNotification(notification)
                            },
                            modifier = modifier
                        )
                    }
                }
            }
            WindowWidthSizeClass.Medium, WindowWidthSizeClass.Expanded -> {
                Row(
                    modifier = modifier
                        .padding(paddingValues)
                        .padding(start = 16.dp, end = 16.dp, bottom = 80.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(modifier = Modifier.fillMaxWidth(0.25f)) {
                        NotificationPageContent(
                            notificationsUiState = viewModel.notificationsUiState.collectAsState().value,
                            onNotificationClick = { notification ->
                                viewModel.selectNotification(notification)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Card(modifier = Modifier.weight(1f)) {
                        if (selectedNotification != null) {
                            NotificationDetailsContent(
                                notification = selectedNotification!!,
                                modifier = Modifier.padding(16.dp)
                            )
                        } else {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Select notification to view",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                }
            }
            else -> {
                Box(modifier = modifier.padding(paddingValues)) {
                    if (selectedNotification != null) {
                        NotificationDetailsContent(
                            notification = selectedNotification!!,
                            modifier = modifier
                        )
                    } else {
                        NotificationPageContent(
                            notificationsUiState = viewModel.notificationsUiState.collectAsState().value,
                            onNotificationClick = { notification ->
                                viewModel.selectNotification(notification)
                            },
                            modifier = modifier
                        )
                    }
                }
            }
        }
    }
}