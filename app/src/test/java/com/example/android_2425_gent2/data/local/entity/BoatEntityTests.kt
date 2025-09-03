package com.example.android_2425_gent2.data.local.entity

import com.example.android_2425_gent2.data.model.Boat
import junit.framework.TestCase.assertEquals
import org.junit.Test

class BoatEntityTests {
    @Test
    fun entityAsExternalModel_returnsBoat() {
        val entity = BoatEntity(
            boatId = 1,
            name = "Boat 1"
        )
        val boat = Boat(
            id = 1,
            name = "Boat 1"
        )
        assertEquals(entity.asExternalModel(), boat)
    }
}