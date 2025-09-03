package com.example.android_2425_gent2.data.repository.timeslot

import com.example.android_2425_gent2.data.remote.model.TimeSlot
import com.example.android_2425_gent2.data.remote.model.TimeSlotResponse

interface TimeSlotRepository {
    suspend fun getTimeSlotsForRange(start:String, end:String): TimeSlotResponse
    suspend fun getTimeSlotsForDay(year: Int, month: Int, day: Int): List<TimeSlot>
}
