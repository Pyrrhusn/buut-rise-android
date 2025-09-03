package com.example.android_2425_gent2.ui.screens.reservations_page.partials

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.example.android_2425_gent2.R
import com.example.android_2425_gent2.data.model.OfflineReservation
import java.util.Locale

@Composable
fun ReservationBoatText(reservation: OfflineReservation, modifier: Modifier) {
    Text(
        "${
            stringResource(R.string.boat).replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }
        }: ${reservation.boatPersonalName}", fontSize = 20.sp
    )
}