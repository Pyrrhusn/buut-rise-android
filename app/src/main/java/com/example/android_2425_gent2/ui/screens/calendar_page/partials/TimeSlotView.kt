package com.example.android_2425_gent2.ui.screens.calendar_page.partials

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.android_2425_gent2.data.remote.model.TimeSlot
import java.time.Duration
import java.time.LocalTime


@Composable
fun TimeSlotView(
    timeSlots: List<TimeSlot>, onTimeSlotClick: (TimeSlot) -> Unit
) {

    val startTime = LocalTime.of(7, 0)
    val endTime = LocalTime.of(21, 0)


    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    // Timeline column
                    Box(modifier = Modifier.width(50.dp)) {
                        Column {
                            for (hour in startTime.hour..endTime.hour) {
                                Text(
                                    text = String.format("%02d:00", hour),
                                    color = Color.Gray,
                                    modifier = Modifier
                                        .height(60.dp)
                                        .padding(top = 8.dp)
                                )
                            }
                        }
                    }

                    // Time slots column
                    Box(modifier = Modifier.weight(1f)) {
                        // Calculate absolute positions for all slots
                        timeSlots.forEach { slot ->
                            val slotStartTime = LocalTime.parse(slot.start)
                            val slotEndTime = LocalTime.parse(slot.end)

                            // Calculate offset from the start of the day
                            val startOffsetMinutes =
                                (slotStartTime.hour - startTime.hour) * 60 + slotStartTime.minute
                            val durationMinutes =
                                Duration.between(slotStartTime, slotEndTime).toMinutes()

                            Box(
                                modifier = Modifier
                                    .offset(y = ((startOffsetMinutes / 60f) * 60).dp)
                                    .fillMaxWidth()
                            ) {
                                TimeSlotItem(
                                    slot = slot,
                                    heightDp = (durationMinutes / 60f) * 60,
                                    onClick = {
                                        if (!slot.isBookedByUser) {
                                            onTimeSlotClick(slot)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}