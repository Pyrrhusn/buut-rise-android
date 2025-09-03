package com.example.android_2425_gent2.ui.screens.reservations_page.partials

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.android_2425_gent2.data.network.model.ReservationDetailsDto
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.colorResource
import com.example.android_2425_gent2.R

@Composable
fun ReservationDetailsAddress(
    details: ReservationDetailsDto,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Adres",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.primary)
        )
        Text("${details.currentHolderStreet} ${details.currentHolderNumber}")
        Text("${details.currentHolderPostalCode} ${details.currentHolderCity}")
    }
}