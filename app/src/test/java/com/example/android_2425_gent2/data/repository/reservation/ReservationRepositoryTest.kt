package com.example.android_2425_gent2.data.repository.reservation

import com.example.android_2425_gent2.data.local.dao.OfflineReservationDao
import com.example.android_2425_gent2.data.network.model.ReservationDetailsDto
import com.example.android_2425_gent2.data.network.reservation.ReservationApiService
import com.example.android_2425_gent2.data.repository.APIResource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class ReservationRepositoryTest {

    private lateinit var repository: OfflineFirstReservationRepository
    private val mockApiService: ReservationApiService = mockk()
    private val mockOfflineDao: OfflineReservationDao = mockk()

    private val mockReservationDetails = ReservationDetailsDto(
        id = 1,
        start = "10:00",
        end = "12:00",
        date = "2024-01-01",
        isDeleted = false,
        boatPersonalName = "Test Boat",
        mentorName = "Test Mentor",
        batteryId = 1,
        currentBatteryUserName = "John Doe",
        currentBatteryUserId = 1,
        currentHolderPhoneNumber = "123456789",
        currentHolderEmail = "john@example.com",
        currentHolderStreet = "Test Street",
        currentHolderNumber = "123",
        currentHolderCity = "Test City",
        currentHolderPostalCode = "1000"
    )

    @Before
    fun setup() {
        repository = OfflineFirstReservationRepository(mockOfflineDao, mockApiService)
    }

    @Test
    fun `getReservationDetails success returns details`() = runTest {
        // Arrange
        coEvery { mockApiService.getReservationDetails(1) } returns Response.success(mockReservationDetails)
        coEvery { mockOfflineDao.getOfflineReservationById(1) } returns flow { emit(null) }

        // Act
        repository.getReservationDetails(1).collect { result ->
            // Assert
            when (result) {
                is APIResource.Loading -> {
                    // First emission should be Loading
                    assertTrue(true)
                }
                is APIResource.Success -> {
                    // Second emission should be Success with mock data
                    assertEquals(mockReservationDetails, result.data)
                }
                else -> {
                    assertTrue("Unexpected result type: $result", false)
                }
            }
        }

        // Verify
        coVerify { mockApiService.getReservationDetails(1) }
        coVerify { mockOfflineDao.getOfflineReservationById(1) }
    }

    @Test
    fun `getReservationDetails error returns error resource`() = runTest {
        // Arrange
        val errorResponse = Response.error<ReservationDetailsDto>(404, mockk(relaxed = true))
        coEvery { mockApiService.getReservationDetails(1) } returns errorResponse
        coEvery { mockOfflineDao.getOfflineReservationById(1) } returns flow { emit(null) }

        // Act
        repository.getReservationDetails(1).collect { result ->
            // Assert
            when (result) {
                is APIResource.Loading -> {
                    assertTrue(true)
                }
                is APIResource.Error -> {
                    result.message?.let { assertTrue(it.contains("Failed to fetch reservation details")) }
                }
                else -> {
                    assertTrue("Unexpected result type: $result", false)
                }
            }
        }

        // Verify
        coVerify { mockApiService.getReservationDetails(1) }
        coVerify { mockOfflineDao.getOfflineReservationById(1) }
    }

    @Test
    fun `getReservationDetails exception returns error resource`() = runTest {
        // Arrange
        coEvery { mockApiService.getReservationDetails(1) } throws Exception("Network error")
        coEvery { mockOfflineDao.getOfflineReservationById(1) } returns flow { emit(null) }

        // Act
        repository.getReservationDetails(1).collect { result ->
            // Assert
            when (result) {
                is APIResource.Loading -> {
                    assertTrue(true)
                }
                is APIResource.Error -> {
                    assertEquals("Failed to fetch reservation details: Network error", result.message)
                }
                else -> {
                    assertTrue("Unexpected result type: $result", false)
                }
            }
        }

        // Verify
        coVerify { mockApiService.getReservationDetails(1) }
        coVerify { mockOfflineDao.getOfflineReservationById(1) }
    }
}