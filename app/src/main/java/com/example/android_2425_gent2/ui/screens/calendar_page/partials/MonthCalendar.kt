package com.example.android_2425_gent2.ui.screens.calendar_page.partials

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android_2425_gent2.data.remote.model.DayInfo
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun MonthCalendar(
    currentMonth: YearMonth,
    onDateSelected: (LocalDate) -> Unit,
    selectedDate: LocalDate?,
    availableDays: List<DayInfo>
) {
    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfWeek = currentMonth.atDay(1).dayOfWeek.value % 7
    val today = LocalDate.now()

    Column {
        WeekdayHeader()
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.height(300.dp)
        ) {
            items(firstDayOfWeek) {
                Box(modifier = Modifier.aspectRatio(1f))
            }
            items(daysInMonth) { day ->
                val date = currentMonth.atDay(day + 1)

                // Find if this day is available from the API response
                val dayInfo = availableDays.find { LocalDate.parse(it.date) == date }

                DayCell(
                    date = date,
                    isSelected = date == selectedDate,
                    isToday = date == today,
                    isAvailable = dayInfo?.isSlotAvailable == true, // Check availability
                    isFullyBooked = dayInfo?.isFullyBooked == true, // Check if fully booked
                    onDateSelected = onDateSelected
                )
            }
        }
    }
}