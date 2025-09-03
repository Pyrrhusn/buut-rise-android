package com.example.android_2425_gent2.ui.screens.reservations_page.partials

import com.example.android_2425_gent2.data.network.model.ReservationDetailsDto
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.android_2425_gent2.R
import com.example.android_2425_gent2.data.model.OfflineReservation
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationDetailsBottomModalSheet(
    selectedReservation: OfflineReservation,
    onSelectedReservationChange: (OfflineReservation?) -> Unit,
    onCancelReservation: (Int) -> Unit,
    reservationDetails: ReservationDetailsDto?,
    isLoading: Boolean,
    modifier: Modifier
) {

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    // Check of annuleren nog mogelijk is (minimaal 2 dagen van tevoren)
    val today = LocalDate.now()
    val canCancel = selectedReservation.date.minusDays(2).isAfter(today)

    ModalBottomSheet(
        onDismissRequest = { onSelectedReservationChange(null) },
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        modifier = Modifier.testTag("ModalBottomSheet")
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = modifier.padding(16.dp)
        ) {
            ReservationDetailsHeader(stringResource(R.string.reservation_details))

            Column {
                ReservationDateText(selectedReservation, modifier)
                ReservationTimeSlotText(selectedReservation, modifier)
                ReservationBoatText(selectedReservation, modifier)
                Spacer(modifier.height(20.dp))

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                            .testTag("LoadingIndicator")
                    )
                } else if (reservationDetails != null) {
                    // Check if any essential details are null or blank
                    val hasValidDetails = !(
                            reservationDetails.currentBatteryUserName.isNullOrBlank() ||
                                    reservationDetails.currentHolderPhoneNumber.isNullOrBlank() ||
                                    reservationDetails.currentHolderEmail.isNullOrBlank() ||
                                    reservationDetails.currentHolderStreet.isNullOrBlank() ||
                                    reservationDetails.currentHolderNumber.isNullOrBlank() ||
                                    reservationDetails.currentHolderCity.isNullOrBlank() ||
                                    reservationDetails.currentHolderPostalCode.isNullOrBlank()
                            )

                    if (hasValidDetails) {
                        ReservationDetailsPersonInfo(reservationDetails, modifier)
                        Spacer(modifier.height(16.dp))
                        ReservationDetailsAddress(reservationDetails, modifier)

                        if (!reservationDetails.mentorName.isNullOrBlank()) {
                            Spacer(modifier.height(16.dp))
                            ReservationDetailsMentor(reservationDetails.mentorName, modifier)
                        }
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = colorResource(R.color.primary)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Geen ophaal informatie beschikbaar",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                color = colorResource(R.color.secondary_contrast_text)
                            )
                        }
                        Spacer(modifier.height(32.dp))
                    }
                }
            }
            Spacer(modifier.height(60.dp))

            // Toon waarschuwing als annuleren niet mogelijk is


            ElevatedButton(
                onClick = {
                    onCancelReservation(selectedReservation.id)
                },
                colors = ButtonColors(
                    Color(0xFFC44244),
                    contentColor = Color.White,
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = Color.Black,
                ),
                enabled = canCancel,  // Hier de canCancel gebruiken ipv true
                modifier = modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
            ) {
                Text(
                    text = if (canCancel)
                        stringResource(R.string.cancel_reservation)
                    else
                        stringResource(R.string.not_cancellable)
                )
            }
        }
    }
}