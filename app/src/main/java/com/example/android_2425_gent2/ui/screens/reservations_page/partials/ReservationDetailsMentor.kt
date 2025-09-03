package com.example.android_2425_gent2.ui.screens.reservations_page.partials

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.android_2425_gent2.R

@Composable
fun ReservationDetailsMentor(
    mentorName: String?,
    modifier: Modifier
) {
    if (!mentorName.isNullOrBlank()) {
        Column(modifier = modifier) {
            Text(
                text = "Meter/Peter",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.primary)
            )
            Text(mentorName)
        }
    }
}