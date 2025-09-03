package com.example.android_2425_gent2.ui.screens.reservations_page.partials

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun ReservationDetailsHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        fontSize = 32.sp,
        modifier = modifier
    )
}
