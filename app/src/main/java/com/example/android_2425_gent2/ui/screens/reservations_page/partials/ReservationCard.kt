package com.example.android_2425_gent2.ui.screens.reservations_page.partials

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android_2425_gent2.data.model.OfflineReservation

@Composable
fun ReservationCard(
    reservation: OfflineReservation,
    onSelectedReservationChange: (OfflineReservation) -> Unit,
    modifier: Modifier
) {

    Card(
        onClick = { onSelectedReservationChange(reservation) },
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(modifier = modifier.padding(8.dp)) {
            ReservationDateText(reservation, modifier)
            ReservationTimeSlotText(reservation, modifier)
            ReservationBoatText(reservation, modifier)
        }
    }
}
