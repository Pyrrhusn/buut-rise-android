package com.example.android_2425_gent2.ui.screens.profile_page

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android_2425_gent2.R
import com.example.android_2425_gent2.ui.AppViewModelProvider
import com.example.android_2425_gent2.ui.common.ErrorMessage
import com.example.android_2425_gent2.ui.screens.profile_page.partials.AppTopBar

@Composable
fun UserDetailsScreen(
    userId: String,
    viewModel: UserDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(userId) {
        viewModel.fetchUserDetails(userId)
    }

    LaunchedEffect(uiState.isUpdateSuccess) {
        if (uiState.isUpdateSuccess) {
            onNavigateBack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(Modifier)
    ) {
        AppTopBar(
            title = stringResource(R.string.user_details),
            canNavigateBack = true,
            onNavigateBack = onNavigateBack
        )

        when {
            uiState.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            uiState.errorMessage.isNotEmpty() -> {
                ErrorMessage(message = uiState.errorMessage)
            }
            uiState.userDetails != null -> {
                val userDetails = uiState.userDetails!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${userDetails.firstName} ${userDetails.familyName}",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(text = "Email: ${userDetails.email}")
                    Text(text = "Phone: ${userDetails.phoneNumber}")

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Address",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(text = "${userDetails.address.street} ${userDetails.address.number}")
                    Text(text = "${userDetails.address.postalCode} ${userDetails.address.city}")
                    Text(text = userDetails.address.country)

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.approveUser(userDetails.id) },
                        modifier = Modifier.align(Alignment.End),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.primary)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Approve"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(R.string.approve))
                    }
                }
            }
        }
    }
}