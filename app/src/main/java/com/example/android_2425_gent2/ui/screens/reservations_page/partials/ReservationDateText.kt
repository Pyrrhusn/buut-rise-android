package com.example.android_2425_gent2.ui.screens.reservations_page.partials

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.example.android_2425_gent2.R
import com.example.android_2425_gent2.data.model.OfflineReservation
import java.util.Locale
import com.example.android_2425_gent2.utils.DATE_FORMATTER

@Composable
fun ReservationDateText(reservation: OfflineReservation, modifier: Modifier) {
    Text(
        "${
            stringResource(R.string.date).replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }
        }: ${reservation.date.format(DATE_FORMATTER)}", fontSize = 20.sp
    )
}