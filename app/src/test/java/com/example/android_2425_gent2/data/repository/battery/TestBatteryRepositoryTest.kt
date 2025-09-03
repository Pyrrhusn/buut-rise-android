package com.example.android_2425_gent2.data.repository.battery

import com.example.android_2425_gent2.data.repository.APIResource
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class TestBatteryRepositoryTest {
    private lateinit var repository: TestBatteryRepository

    @Before
    fun setup() {
        repository = TestBatteryRepository()
    }

    @Test
    fun getAllBatteries_returnsExpectedTestData() = runTest {
        val result = repository.getAllBatteries().first()

        assertTrue(result is APIResource.Success)
        result as APIResource.Success

        assertEquals(2, result.data?.size)
        assertEquals(1, result.data?.first()?.id)
        assertEquals("Lithium-Ion", result.data?.first()?.type)
        assertEquals("Doe, John", result.data?.first()?.mentor?.fullName)
    }

    @Test
    fun getBatteriesByBoat_returnsExpectedTestData() = runTest {
        val result = repository.getBatteriesByBoat(1).first()

        assertTrue(result is APIResource.Success)
        result as APIResource.Success

        assertEquals(2, result.data?.size)
        assertEquals(1, result.data?.first()?.id)
        assertEquals("Lithium-Ion", result.data?.first()?.type)
    }

    @Test
    fun assignMentor_returnsSuccess() = runTest {
        val result = repository.assignMentor(1, 1).first()

        assertTrue(result is APIResource.Success)
    }
}