package com.example.android_2425_gent2.ui.screens.calendar_page.partials

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.android_2425_gent2.R
import java.time.LocalDate


@Composable
fun DayCell(
    date: LocalDate,
    isSelected: Boolean,
    isToday: Boolean,
    isAvailable: Boolean,  // From API response
    isFullyBooked: Boolean, // From API response
    onDateSelected: (LocalDate) -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .clip(CircleShape)
            .background(
                when {
                    isSelected -> colorResource(R.color.primary)
                    else -> Color.Transparent
                }
            )
            .border(1.dp, if (isSelected) colorResource(R.color.primary) else Color.Transparent, CircleShape)
            .clickable(enabled = isAvailable && !isFullyBooked) { onDateSelected(date) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            color = when {
                isSelected -> Color.White
                !isAvailable || isFullyBooked -> Color.LightGray
                else -> Color.Black
            },
            fontWeight = if (isAvailable && !isFullyBooked) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

