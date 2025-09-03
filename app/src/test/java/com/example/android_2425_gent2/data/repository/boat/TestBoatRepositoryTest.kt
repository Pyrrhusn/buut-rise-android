package com.example.android_2425_gent2.data.repository.boat

import com.example.android_2425_gent2.data.repository.APIResource
import junit.framework.Assert.assertTrue
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TestBoatRepositoryTest {
    private lateinit var repository: TestBoatRepository

    @Before
    fun setup() {
        repository = TestBoatRepository()
    }

    @Test
    fun getBoats_returnsExpectedTestData() = runTest {
        val result = repository.getBoats().first()

        assertTrue(result is APIResource.Success)
        result as APIResource.Success

        assertEquals(3, result.data?.size)
        with(result.data?.first()) {
            assertEquals(1, this?.id)
            assertEquals("Test Boat 1", this?.name)
            assertEquals("Limba", this?.personalName)
            assertTrue(this?.isAvailable == true)
        }
    }

    @Test
    fun getBoats_returnsLoadingStateWhenSet() = runTest {
        repository.setLoading(true)
        val result = repository.getBoats().first()

        assertTrue(result is APIResource.Loading)
    }

    @Test
    fun getBoats_returnsErrorStateWhenSet() = runTest {
        val errorMessage = "Test error"
        repository.setError(errorMessage)
        val result = repository.getBoats().first()

        assertTrue(result is APIResource.Error)
        assertEquals(errorMessage, (result as APIResource.Error).message)
    }

    @Test
    fun setLoading_clearsErrorState() = runTest {
        repository.setError("Test error")
        repository.setLoading(true)

        val result = repository.getBoats().first()
        assertTrue(result is APIResource.Loading)
    }

    @Test
    fun setError_clearsLoadingState() = runTest {
        repository.setLoading(true)
        repository.setError("Test error")

        val result = repository.getBoats().first()
        assertTrue(result is APIResource.Error)
        assertFalse((result as APIResource.Error).message.isNullOrEmpty())
    }
}