package com.example.android_2425_gent2.ui.screens.reservations_page.partials

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android_2425_gent2.data.network.model.ReservationDetailsDto
import androidx.compose.ui.res.colorResource
import com.example.android_2425_gent2.R

@Composable
fun ReservationDetailsPersonInfo(
    details: ReservationDetailsDto,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Gegevens ophaal persoon",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.primary)
        )
        Text("Naam: ${details.currentBatteryUserName}")
        Text("Tel.: ${details.currentHolderPhoneNumber}")
        Text("E-mail: ${details.currentHolderEmail}")
    }
}