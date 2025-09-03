package com.example.android_2425_gent2.ui.screens.profile_page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android_2425_gent2.R
import com.example.android_2425_gent2.ui.AppViewModelProvider
import com.example.android_2425_gent2.ui.common.ErrorMessage
import com.example.android_2425_gent2.ui.screens.profile_page.partials.AppTopBar
import com.example.android_2425_gent2.ui.screens.profile_page.partials.LogOutButton
import com.example.android_2425_gent2.ui.screens.profile_page.partials.ProfileActionCard
import com.example.android_2425_gent2.ui.screens.profile_page.partials.ProfileHeader
import com.example.android_2425_gent2.ui.screens.profile_page.partials.ProfileNavigationButton

@Composable
fun ProfilePage(
    onNavigateToDashboard: () -> Unit,
    modifier: Modifier = Modifier,
    logout: () -> Unit
    ) {

    val viewModel: ProfilePageViewModel = viewModel(
        factory = AppViewModelProvider.Factory,
    )

    val uiState by viewModel.uiState.collectAsState()


    Column {
        AppTopBar(title=stringResource(R.string.profile))

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                    ProfileHeader(
                        name = uiState.user.firstName + " " + uiState.user.familyName,
                        email = uiState.user.email
                    )

                    Spacer(modifier = Modifier.height(64.dp))

                    val profileActions = listOf(
                        ProfileAction(
                            icon = Icons.Default.Edit,
                            text = stringResource(R.string.edit_profile),
                            onClick = { /* TODO: handle this later*/ }
                        ),
                        ProfileAction(
                            icon = Icons.Default.Lock,
                            text = stringResource(R.string.privacy_settings),
                            onClick = { /* TODO: handle this later*/ }
                        )
                    )

                    ProfileActionCard(actions = profileActions)

                    Spacer(modifier = Modifier.height(64.dp))

                    if (uiState.isAdmin) {
                        ProfileNavigationButton(
                            text = stringResource(R.string.go_to_dashboard),
                            icon = Icons.Default.AccountBox,
                            onClick = onNavigateToDashboard
                        )

                        Spacer(modifier = Modifier.height(72.dp))
                    }

                    LogOutButton(
                        onClick = {
                            viewModel.handleLogout {
                                logout()
                            }
                        }
                    )
                }
            }
        }
    }
}

/*require this for a action in profile page*/
data class ProfileAction(
    val icon: ImageVector,
    val text: String,
    val onClick: () -> Unit
)