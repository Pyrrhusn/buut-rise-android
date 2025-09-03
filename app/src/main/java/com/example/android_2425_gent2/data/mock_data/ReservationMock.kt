package com.example.android_2425_gent2.data.mock_data

import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth

data class TimeSlot(
    val startTime: LocalTime,
    val endTime: LocalTime,
    val isAvailable: Boolean = true,
    val isYourReservation: Boolean = false
)

data class DayReservation(
    val date: LocalDate,
    val timeSlots: List<TimeSlot>,
    val isFullyBooked: Boolean = false,
    val hasYourReservation: Boolean = false
)

object ReservationMock {
    private val currentMonth = YearMonth.now()
    private val reservations = mutableMapOf<LocalDate, DayReservation>()

    init {
        generateMockData()
    }

    private fun generateMockData() {
        val daysInMonth = currentMonth.lengthOfMonth()
        val yourReservationDays = (1..daysInMonth).shuffled().take(3)

        for (day in 1..daysInMonth) {
            val date = currentMonth.atDay(day)
            val slots = when {
                yourReservationDays.contains(day) -> generateYourReservationDay()
                (1..5).random() == 1 -> generateFullyBookedDay() // 20% chance of fully booked day
                else -> generateRandomAvailability()
            }
            val isFullyBooked = slots.none { it.isAvailable }
            val hasYourReservation = slots.any { it.isYourReservation }
            reservations[date] = DayReservation(date, slots, isFullyBooked, hasYourReservation)
        }
    }

    private fun generateYourReservationDay(): List<TimeSlot> {
        val yourReservationSlot = (0..2).random()
        return List(3) { index ->
            TimeSlot(
                startTime = LocalTime.of(9 + index * 3, 0),
                endTime = LocalTime.of(12 + index * 3, 0),
                isAvailable = index != yourReservationSlot,
                isYourReservation = index == yourReservationSlot
            )
        }
    }

    private fun generateFullyBookedDay(): List<TimeSlot> {
        return List(3) { index ->
            TimeSlot(
                startTime = LocalTime.of(9 + index * 3, 0),
                endTime = LocalTime.of(12 + index * 3, 0),
                isAvailable = false
            )
        }
    }

    private fun generateRandomAvailability(): List<TimeSlot> {
        return List(3) { index ->
            TimeSlot(
                startTime = LocalTime.of(9 + index * 3, 0),
                endTime = LocalTime.of(12 + index * 3, 0),
                isAvailable = (0..2).random() != 0 // 2/3 chance of being available
            )
        }
    }

    fun getReservationsForDate(date: LocalDate): DayReservation? {
        return reservations[date]
    }
}
