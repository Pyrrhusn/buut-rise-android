package com.example.android_2425_gent2.ui.screens.profile_page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android_2425_gent2.data.network.battery.BatteryDto
import com.example.android_2425_gent2.data.network.boat.BoatDto
import com.example.android_2425_gent2.ui.AppViewModelProvider
import com.example.android_2425_gent2.ui.screens.profile_page.partials.AppTopBar
import androidx.compose.ui.res.stringResource
import com.example.android_2425_gent2.R
import com.example.android_2425_gent2.ui.common.ErrorMessage
import com.example.android_2425_gent2.ui.common.LoadingIndicator

@Composable
fun BoatManagementScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BoatManagementViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Column {
        AppTopBar(
            title = stringResource(R.string.manage_batteries),
            canNavigateBack = true,
            onNavigateBack = onNavigateBack
        )

        when {
            uiState.isLoading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LoadingIndicator()
                }
            }
            uiState.errorMessage.isNotEmpty() -> {
                ErrorMessage(message = uiState.errorMessage)
            }
            uiState.selectedBoatId == null -> {
                // Show list of boats
                BoatsList(
                    boats = uiState.boats,
                    onBoatSelected = { viewModel.selectBoat(it) }
                )
            }
            else -> {
                // Show batteries for selected boat
                BatteriesList(
                    batteries = uiState.batteries,
                    users = uiState.users,
                    onAssignMentor = { batteryId, mentorId -> 
                        viewModel.assignMentor(batteryId, mentorId)
                    }
                )
            }
        }
    }
}

@Composable
private fun BoatsList(
    boats: List<BoatDto>,
    onBoatSelected: (Int) -> Unit
) {
    LazyColumn {
        items(boats) { boat ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { onBoatSelected(boat.id) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = boat.personalName?.toString().orEmpty(),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = boat.name?.toString().orEmpty(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
} 