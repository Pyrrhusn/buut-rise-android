package com.example.android_2425_gent2.ui.screens.reservations_page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android_2425_gent2.R
import com.example.android_2425_gent2.ui.AppViewModelProvider
import com.example.android_2425_gent2.ui.common.ErrorMessage
import com.example.android_2425_gent2.ui.screens.reservations_page.partials.ReservationDetailsBottomModalSheet
import com.example.android_2425_gent2.ui.screens.reservations_page.partials.ReservationList
import com.example.android_2425_gent2.ui.screens.reservations_page.partials.ReservationTypeSelectionDropDownMenu
import kotlinx.coroutines.launch

@Preview
@Composable
fun ReservationsPage(
    viewModel: ReservationsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val reservationsUiState by viewModel.reservationsUiState.collectAsState()
    val selectedReservationUiState = viewModel.selectedReservationUiState
    val reservationTypeUiState = viewModel.reservationTypeUiState

    Column(modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        ReservationTypeSelectionDropDownMenu(
            reservationTypeUiState.reservationType,
            {
                viewModel.setReservationType(it)
            },
            modifier = modifier.testTag("ReservationTypeSelectionDropDownMenu"),
        )

        if (reservationsUiState.hasError) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.fillMaxSize()
            ) {
                ErrorMessage(stringResource(R.string.the_reservations_could_not_be_loaded))
            }
        } else if (reservationsUiState.loading && reservationsUiState.reservations.isEmpty()) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        } else {
            ReservationList(
                reservations = reservationsUiState.reservations,
                onSelectedReservationChange = { reservation ->
                    coroutineScope.launch {
                        viewModel.setSelectedReservation(reservation)
                    }
                },
                modifier = modifier,
            )
        }

        if (selectedReservationUiState.selectedReservation != null &&
            !selectedReservationUiState.selectedReservation.isDeleted &&
            reservationTypeUiState.reservationType != ReservationType.OLD) {
            ReservationDetailsBottomModalSheet(
                selectedReservation = selectedReservationUiState.selectedReservation,
                reservationDetails = selectedReservationUiState.details,
                isLoading = selectedReservationUiState.isLoadingDetails,
                onSelectedReservationChange = {
                    coroutineScope.launch {
                        viewModel.setSelectedReservation(it)
                    }
                },
                onCancelReservation = { reservationId ->
                    viewModel.cancelReservation(reservationId)
                },
                modifier = modifier
            )
        }
    }
}

enum class ReservationType(val title: Int) {
    OLD(title = R.string.old_reservations),
    UPCOMING(title = R.string.upcoming_reservations),
    CANCELED(title = R.string.canceled_reservations),
}