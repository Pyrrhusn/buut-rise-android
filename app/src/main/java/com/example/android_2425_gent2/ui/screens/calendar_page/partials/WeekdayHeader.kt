package com.example.android_2425_gent2.ui.screens.calendar_page.partials

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun WeekdayHeader() {
    Row(modifier = Modifier.fillMaxWidth()) {
        listOf("Ma", "Di", "Wo", "Do", "Vr", "Za", "Zo").forEach { day ->
            Text(
                text = day,
                modifier = Modifier.weight(1f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
        }
    }
}
