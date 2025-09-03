package com.example.android_2425_gent2.ui.screens.profile_page

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android_2425_gent2.R
import com.example.android_2425_gent2.ui.AppViewModelProvider
import com.example.android_2425_gent2.ui.common.ErrorMessage
import com.example.android_2425_gent2.ui.screens.profile_page.partials.AppTopBar
import com.example.android_2425_gent2.ui.screens.profile_page.partials.GuestUsersList

@Composable
fun GuestUsersScreen(
    viewModel: GuestUsersViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onNavigateToUserDetails: (String) -> Unit,
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchUsers()
    }

    Column() {
        AppTopBar(
            title = stringResource(R.string.users_list),
            canNavigateBack = true,
            onNavigateBack = onNavigateBack)

        when {
            uiState.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            uiState.errorMessage.isNotEmpty() -> {
                ErrorMessage(
                    message = uiState.errorMessage,
                )
            }
            else -> {
                GuestUsersList(
                    users = uiState.users,
                    onUserClick = onNavigateToUserDetails,
                    modifier = Modifier
                )
            }
        }
    }
}