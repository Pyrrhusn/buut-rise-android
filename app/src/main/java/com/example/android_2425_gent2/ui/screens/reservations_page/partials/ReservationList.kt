package com.example.android_2425_gent2.ui.screens.reservations_page.partials

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android_2425_gent2.data.model.OfflineReservation

@Composable
fun ReservationList(
    reservations: List<OfflineReservation>,
    onSelectedReservationChange: (OfflineReservation) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            items = reservations,
            key = { it.id }
        ) { reservation ->
            ReservationCard(
                reservation = reservation,
                onSelectedReservationChange = onSelectedReservationChange,
                modifier = modifier
            )
        }
    }
}