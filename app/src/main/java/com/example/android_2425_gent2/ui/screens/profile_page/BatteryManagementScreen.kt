package com.example.android_2425_gent2.ui.screens.profile_page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.android_2425_gent2.data.network.battery.BatteryDto
import com.example.android_2425_gent2.data.network.users.UserNameDto
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android_2425_gent2.R
import com.example.android_2425_gent2.ui.AppViewModelProvider
import com.example.android_2425_gent2.ui.common.ErrorMessage
import com.example.android_2425_gent2.ui.common.LoadingIndicator
import com.example.android_2425_gent2.ui.screens.profile_page.partials.AppTopBar
import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.testTag

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BatteryManagementScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BatteryManagementViewModel = viewModel(factory = AppViewModelProvider.Factory)
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
                    LoadingIndicator(
                        modifier = Modifier.testTag("LoadingIndicator")
                    )
                }
            }
            uiState.errorMessage.isNotEmpty() -> {
                ErrorMessage(message = uiState.errorMessage)
            }
            else -> {
                BatteriesList(
                    batteries = uiState.batteries,
                    users = uiState.users,
                    onAssignMentor = { batteryId, mentorId -> 
                        viewModel.assignMentor(batteryId, mentorId)
                    }
                )
            }
        }

        if (uiState.successMessage.isNotEmpty()) {
            LaunchedEffect(uiState.successMessage) {
                delay(2000)
                viewModel.clearSuccessMessage()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BatteriesList(
    batteries: List<BatteryDto>,
    users: List<UserNameDto>,
    onAssignMentor: (Int, Int) -> Unit
) {
    LazyColumn {
        items(batteries.sortedBy { it.id }) { battery ->
            BatteryCard(
                battery = battery,
                users = users,
                onAssignMentor = onAssignMentor
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BatteryCard(
    battery: BatteryDto,
    users: List<UserNameDto>,
    onAssignMentor: (Int, Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var selectedUser by remember { mutableStateOf<UserNameDto?>(null) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Batterij ID: ${battery.id}",
                style = MaterialTheme.typography.headlineSmall
            )
            
            Text(
                text = "Type: ${battery.type}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
            
            Text(
                text = "Huidige Meter/Peter: ${battery.mentor?.fullName ?: "Geen meter/peter toegewezen"}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    value = selectedUser?.fullName ?: "Selecteer Meter/Peter",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.heightIn(max = 300.dp)
                ) {
                    users.take(8).forEach { user ->
                        DropdownMenuItem(
                            text = { Text(text = user.fullName) },
                            onClick = {
                                selectedUser = user
                                expanded = false
                                showConfirmDialog = true
                            }
                        )
                    }
                }
            }
        }
    }

    if (showConfirmDialog && selectedUser != null) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Meter/Peter Toewijzen") },
            text = { Text("Weet u zeker dat u ${selectedUser?.fullName} als meter/peter wilt toewijzen?") },
            confirmButton = {
                TextButton(onClick = {
                    onAssignMentor(battery.id, selectedUser!!.id)
                    showConfirmDialog = false
                }) {
                    Text("Bevestigen")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Annuleren")
                }
            }
        )
    }
}