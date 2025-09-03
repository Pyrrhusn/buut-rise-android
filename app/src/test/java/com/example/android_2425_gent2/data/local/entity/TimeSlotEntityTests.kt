package com.example.android_2425_gent2.data.local.entity

import com.example.android_2425_gent2.data.model.TimeSlot
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class TimeSlotEntityTests {
    @Test
    fun entityAsExternalModel_returnsTimeSlot() {
        val entity = TimeSlotEntity(
            timeSlotId = 1,
            date = LocalDate.of(2024, 1, 1),
            start = LocalTime.of(0, 0),
            end = LocalTime.of(0, 0)
        )
        val timeSlot = TimeSlot(
            id = 1,
            date = LocalDate.of(2024, 1, 1),
            start = LocalTime.of(0, 0),
            end = LocalTime.of(0, 0)
        )
        assertEquals(entity.asExternalModel(), timeSlot)
    }
}