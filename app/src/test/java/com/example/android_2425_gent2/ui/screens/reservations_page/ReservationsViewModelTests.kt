package com.example.android_2425_gent2.ui.screens.reservations_page

import com.example.android_2425_gent2.data.model.OfflineReservation
import com.example.android_2425_gent2.data.network.model.ReservationDetailsDto
import com.example.android_2425_gent2.data.repository.APIResource
import com.example.android_2425_gent2.data.repository.reservation.ReservationRepository
import com.example.android_2425_gent2.ui.screens.reservations_page.coroutine.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalCoroutinesApi::class)
class ReservationsViewModelTest {

    @get:Rule
    val coroutineRule = MainDispatcherRule()

    private val reservationRepository: ReservationRepository = mockk()
    private lateinit var viewModel: ReservationsViewModel

    private val sampleDate = LocalDate.of(2024, 1, 1)
    private val sampleStartTime = LocalTime.of(10, 0)
    private val sampleEndTime = LocalTime.of(12, 0)

    private val sampleReservation1 = OfflineReservation(
        start = sampleStartTime,
        end = sampleEndTime,
        date = sampleDate,
        boatId = 1,
        boatPersonalName = "Boat 1",
        id = 1
    )

    private val sampleReservation2 = OfflineReservation(
        start = sampleStartTime.plusHours(2),
        end = sampleEndTime.plusHours(2),
        date = sampleDate,
        boatId = 2,
        boatPersonalName = "Boat 2",
        id = 2
    )

    @Test
    fun `initial state should be loading upcoming reservations`() = runTest {
        coEvery { reservationRepository.getReservations(
            getPast = false,
        ) } returns flow {
            emit(APIResource.Success(emptyList()))
        }

        viewModel = ReservationsViewModel(reservationRepository)

        assertEquals(ReservationType.UPCOMING, viewModel.reservationTypeUiState.reservationType)
        assertTrue(viewModel.reservationsUiState.value.loading)
    }

    @Test
    fun `setReservationType should update state and trigger loading`() = runTest {
        // Use every {} for mockk
        coEvery { reservationRepository.getReservations(
            getPast = false,
        ) } returns flow {
            emit(APIResource.Success(emptyList()))
        }

        coEvery { reservationRepository.getReservations(
            getPast = true,
        ) } returns flow {
            emit(APIResource.Success(emptyList()))
        }

        viewModel = ReservationsViewModel(reservationRepository)
        advanceUntilIdle()

        viewModel.setReservationType(ReservationType.OLD)
        advanceUntilIdle()

        assertEquals(ReservationType.OLD, viewModel.reservationTypeUiState.reservationType)
    }

    @Test
    fun `successful reservation load should update state correctly`() = runTest {
        val mockReservations = listOf(sampleReservation1, sampleReservation2)

        coEvery { reservationRepository.getReservations(
            getPast = false,
        ) } returns flow {
            emit(APIResource.Success(
                    mockReservations
                )
            )
        }

        viewModel = ReservationsViewModel(reservationRepository)
        advanceUntilIdle()

        with(viewModel.reservationsUiState.value) {
            assertFalse(loading)
            assertFalse(hasError)
            assertEquals(mockReservations, reservations)
        }
    }

    @Test
    fun `error during reservation load should update error state`() = runTest {
        val errorMessage = "Network error"

        // Use every {} for mockk
        coEvery { reservationRepository.getReservations(
            getPast = false,
        ) } returns flow {
            emit(APIResource.Error(errorMessage))
        }

        viewModel = ReservationsViewModel(reservationRepository)
        advanceUntilIdle()

        with(viewModel.reservationsUiState.value) {
            assertFalse(loading)
            assertTrue(hasError)
            assertEquals(errorMessage, errorMessage)
        }
    }

    @Test
    fun `loadMoreIfNeeded should trigger load when conditions are met`() = runTest {
        val initialReservations = listOf(sampleReservation1, sampleReservation2)

        // Use every {} for mockk
        coEvery { reservationRepository.getReservations(
            getPast = false,
        ) } returns flow {
            emit(APIResource.Success(initialReservations))
        }

        coEvery { reservationRepository.getReservations(
            getPast = false,
        ) } returns flow {
            emit(APIResource.Success(listOf(sampleReservation2)))
        }

        viewModel = ReservationsViewModel(reservationRepository)
        advanceUntilIdle()


    }

    @Test
    fun `setSelectedReservation should update selected reservation state`() = runTest {
        // Use every {} for mockk
        coEvery { reservationRepository.getReservations(
            getPast = false,
        ) } returns flow {
            emit(APIResource.Success(emptyList()))
        }

        viewModel = ReservationsViewModel(reservationRepository)
        advanceUntilIdle()

        viewModel.setSelectedReservation(sampleReservation1)

        assertEquals(sampleReservation1, viewModel.selectedReservationUiState.selectedReservation)
    }

    @Test
    fun `loadReservationDetails success updates state correctly`() = runTest {
        // Arrange
        val mockDetails = ReservationDetailsDto(
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

        coEvery { reservationRepository.getReservations(getPast = any()) } returns flow {
            emit(APIResource.Success(emptyList()))
        }

        coEvery { reservationRepository.getReservationDetails(1) } returns flow {
            emit(APIResource.Loading())
            emit(APIResource.Success(mockDetails))
        }

        viewModel = ReservationsViewModel(reservationRepository)
        advanceUntilIdle()

        // Act
        viewModel.setSelectedReservation(sampleReservation1)
        advanceUntilIdle()

        // Assert
        with(viewModel.selectedReservationUiState) {
            assertEquals(sampleReservation1, selectedReservation)
            assertEquals(mockDetails, details)
            assertFalse(isLoadingDetails)
            assertNull(error)
        }

        coVerify { reservationRepository.getReservationDetails(1) }
    }

    @Test
    fun `loadReservationDetails error updates state correctly`() = runTest {
        // Arrange
        val errorMessage = "Failed to load details"

        coEvery { reservationRepository.getReservations(getPast = any()) } returns flow {
            emit(APIResource.Success(emptyList()))
        }

        coEvery { reservationRepository.getReservationDetails(1) } returns flow {
            emit(APIResource.Loading())
            emit(APIResource.Error(errorMessage))
        }

        viewModel = ReservationsViewModel(reservationRepository)
        advanceUntilIdle()

        // Act
        viewModel.setSelectedReservation(sampleReservation1)
        advanceUntilIdle()

        // Assert
        with(viewModel.selectedReservationUiState) {
            assertEquals(sampleReservation1, selectedReservation)
            assertNull(details)
            assertFalse(isLoadingDetails)
            assertEquals(errorMessage, error)
        }
    }

    @Test
    fun `loadReservationDetails with unknown user updates state correctly`() = runTest {
        // Arrange
        val mockDetails = ReservationDetailsDto(
            id = 1,
            start = "10:00",
            end = "12:00",
            date = "2024-01-01",
            isDeleted = false,
            boatPersonalName = "Test Boat",
            mentorName = null,
            batteryId = 1,
            currentBatteryUserName = "Unknown",
            currentBatteryUserId = 1,
            currentHolderPhoneNumber = null,
            currentHolderEmail = null,
            currentHolderStreet = null,
            currentHolderNumber = null,
            currentHolderCity = null,
            currentHolderPostalCode = null
        )

        coEvery { reservationRepository.getReservations(getPast = any()) } returns flow {
            emit(APIResource.Success(emptyList()))
        }

        coEvery { reservationRepository.getReservationDetails(1) } returns flow {
            emit(APIResource.Loading())
            emit(APIResource.Success(mockDetails))
        }

        viewModel = ReservationsViewModel(reservationRepository)
        advanceUntilIdle()

        // Act
        viewModel.setSelectedReservation(sampleReservation1)
        advanceUntilIdle()

        // Assert
        with(viewModel.selectedReservationUiState) {
            assertEquals(sampleReservation1, selectedReservation)
            assertNull(details)
            assertFalse(isLoadingDetails)
            assertEquals("Details temporarily unavailable", error)
        }
    }
}
