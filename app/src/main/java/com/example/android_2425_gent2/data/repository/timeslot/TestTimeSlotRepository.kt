package com.example.android_2425_gent2.data.repository.timeslot

import com.example.android_2425_gent2.data.remote.model.DayInfo
import com.example.android_2425_gent2.data.remote.model.TimeSlot
import com.example.android_2425_gent2.data.remote.model.TimeSlotResponse
import java.time.LocalDate
import java.time.YearMonth

class TestTimeSlotRepository : TimeSlotRepository {
    override suspend fun getTimeSlotsForRange(start: String, end: String): TimeSlotResponse {
        val startDate = LocalDate.parse(start)
        val endDate = LocalDate.parse(end)
        val currentMonth = YearMonth.from(startDate)

        val days = mutableListOf<DayInfo>()
        var currentDate = startDate
        while (currentDate.isBefore(endDate) || currentDate.isEqual(endDate)) {
            val isSlotAvailable = currentDate.dayOfMonth % 2 == 1
            val isFullyBooked = currentDate.dayOfMonth % 2 == 0
            days.add(
                DayInfo(
                    date = currentDate.toString(),
                    isSlotAvailable = isSlotAvailable,
                    isFullyBooked = isFullyBooked
                )
            )
            currentDate = currentDate.plusDays(1)
        }

        return TimeSlotResponse(
            start = startDate.toString(),
            end = endDate.toString(),
            days = days,
            totalDays = days.size
        )
    }

    override suspend fun getTimeSlotsForDay(year: Int, month: Int, day: Int): List<TimeSlot> {
        val date = LocalDate.of(year, month, day)
        return if (date.dayOfMonth % 2 == 1) {
            listOf(
                TimeSlot(id = 1, start = "09:00", end = "11:30", isBookedByUser = false),
            )
        } else {
            emptyList()
        }
    }
}