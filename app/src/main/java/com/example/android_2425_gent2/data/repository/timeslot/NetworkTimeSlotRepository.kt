package com.example.android_2425_gent2.data.repository.timeslot

import com.example.android_2425_gent2.data.network.timeslot.TimeSlotApiService
import com.example.android_2425_gent2.data.remote.model.TimeSlot
import com.example.android_2425_gent2.data.remote.model.TimeSlotResponse


class NetworkTimeSlotRepository(
    private val timeSlotApiService: TimeSlotApiService
) : TimeSlotRepository {

    override suspend fun getTimeSlotsForRange(
        startDate: String,
        endDate: String
    ): TimeSlotResponse =
        timeSlotApiService.getTimeSlots(startDate, endDate)



    override suspend fun getTimeSlotsForDay(
        year: Int,
        month: Int,
        day: Int
    ): List<TimeSlot> =
        timeSlotApiService.getTimeSlotsForDay(year, month, day)
}