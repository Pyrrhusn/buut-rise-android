package com.example.android_2425_gent2.ui.screens.calendar_page.partials

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android_2425_gent2.R
import com.example.android_2425_gent2.data.remote.model.TimeSlot
import com.example.android_2425_gent2.ui.screens.calendar_page.ReservationState
import java.time.LocalDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeSlotReservationConfirmationSheet(
    timeSlot: TimeSlot,
    reservationState: ReservationState,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    reservationErrorMessage: String = "",
    dayInfo: LocalDate?
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = {
            reservationState != ReservationState.PAYMENT_LOADING
        }
    )
    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = sheetState
    ) {
        when (reservationState) {
            ReservationState.PAYMENT_LOADING -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(32.dp)
                ) {
                    CircularProgressIndicator(
                        color = colorResource(R.color.primary),
                        modifier = Modifier
                            .size(64.dp),
                        )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(R.string.processing_payment),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(R.string.wait_for_process_your_payment),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )
                }
            }

            ReservationState.ERROR -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Error",
                        tint = Color.Red,
                        modifier = Modifier.size(64.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(R.string.reservation_failed),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color.Red
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = reservationErrorMessage.ifEmpty { stringResource(R.string.error_while_processing_reservation) },
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        color = colorResource(R.color.secondary_contrast_text)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    PrimaryButtonWithText(
                        onClick = { onDismiss() },
                        text = stringResource(R.string.close_reservation_details)
                    )
                }
            }

            ReservationState.CONFIRMATION -> {
                Column(
                    modifier = modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.placed_reservation),
                        fontSize = 32.sp,
                    )
                    Box(modifier.height(16.dp))

                    Text(
                        text = dayInfo.toString(),
                        fontSize = 20.sp,
                    )


                    Text(
                        timeSlot.start
                                + " - " +
                                timeSlot.end,
                        fontSize = 32.sp
                    )



                    Spacer(modifier = Modifier.height(32.dp))

                    /* TODO: api call to get user info */
                    Text("Naam: Phillipe van Achter")
                    Text("Tel.: +32 478 85 74 75")
                    Text("E-mail: phillipe.van.achter@gmail.com")

                    Spacer(modifier = Modifier.height(32.dp))

                    PrimaryButtonWithText(
                        onClick = { onDismiss() },
                        text = stringResource(R.string.close_reservation_details)
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }


            else -> {}
        }
    }
}
