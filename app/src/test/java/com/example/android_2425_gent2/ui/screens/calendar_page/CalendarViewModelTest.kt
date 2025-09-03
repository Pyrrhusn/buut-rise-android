package com.example.android_2425_gent2.ui.screens.calendar_page

import com.example.android_2425_gent2.data.remote.model.DayInfo
import com.example.android_2425_gent2.data.remote.model.TimeSlot
import com.example.android_2425_gent2.data.remote.model.TimeSlotResponse
import com.example.android_2425_gent2.data.repository.APIResource
import com.example.android_2425_gent2.data.repository.reservation.ReservationRepository
import com.example.android_2425_gent2.data.repository.timeslot.TimeSlotRepository
import com.example.android_2425_gent2.ui.screens.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate
import java.time.YearMonth

@OptIn(ExperimentalCoroutinesApi::class)
class CalendarViewModelTest {

    @get:Rule
    val coroutineRule = MainDispatcherRule()

    private lateinit var viewModel: CalendarViewModel

    // Mock repository
    private val mockTimeSlotRepositoryRepository: TimeSlotRepository = mockk()
    private val mockReservationRepository: ReservationRepository = mockk()

    @Before
    fun setup() {
        viewModel = CalendarViewModel(
            timeSlotRepository = mockTimeSlotRepositoryRepository,
            reservationRepository = mockReservationRepository
        )
    }

    @Test
    fun changeMonth_Success() = runTest {
        // Arrange
        val newMonth = YearMonth.of(2024, 10)
        val testDays = listOf(
            DayInfo(date = "2024-10-01", isSlotAvailable = true, isFullyBooked = false),
            DayInfo(date = "2024-10-02", isSlotAvailable = false, isFullyBooked = true)
        )

        val timeSlotResponse = TimeSlotResponse(
            start = "2024-10-01",
            end = "2024-10-31",
            days = testDays,
            totalDays = testDays.size
        )

        coEvery { mockTimeSlotRepositoryRepository.getTimeSlotsForRange("2024-10-01", "2024-10-31") } returns timeSlotResponse

        // Act
        viewModel.changeMonth(newMonth)

        // Wait for the UI state to be updated
        val uiState = viewModel.uiState.first { it.monthTimeSlots.isNotEmpty() }

        // Assert
        assertEquals(newMonth, uiState.currentMonth)
        assertEquals(testDays, uiState.monthTimeSlots)

        
        coVerify { mockTimeSlotRepositoryRepository.getTimeSlotsForRange("2024-10-01", "2024-10-31") }
    }

    @Test
    fun fetchTimeSlotsForDay_Success() = runTest {
        // Arrange
        val selectedDate = LocalDate.of(2024, 10, 15)
        val testTimeSlots = listOf(
            TimeSlot(id = 1, start = "09:00", end = "10:00", isBookedByUser = false),
            TimeSlot(id = 2, start = "11:00", end = "12:00", isBookedByUser = false)
        )
        coEvery { mockTimeSlotRepositoryRepository.getTimeSlotsForDay(2024, 10, 15) } returns testTimeSlots

        // Act
        viewModel.selectDate(selectedDate)
        val uiState = viewModel.uiState.first { it.dailyTimeSlots.isNotEmpty() }

        // Assert
        assertEquals(selectedDate, uiState.selectedDate)
        assertEquals(testTimeSlots, uiState.dailyTimeSlots)

        
        coVerify { mockTimeSlotRepositoryRepository.getTimeSlotsForDay(2024, 10, 15) }
    }

    @Test
    fun fetchTimeSlotsForMonth_Error() = runTest {
        // Arrange
        val month = YearMonth.of(2024, 11)
        val errorMessage = "Network error"

        coEvery { mockTimeSlotRepositoryRepository.getTimeSlotsForRange(any(), any()) } throws RuntimeException(errorMessage)

        // Act
        viewModel.changeMonth(month)

        // Wait until monthErrorMessage is updated with the expected error message
        val uiState = viewModel.uiState.first {
            it.monthErrorMessage.isNotEmpty() && !it.isLoadingMonth
        }

        // Assert
        assertTrue(uiState.monthTimeSlots.isEmpty())

        
        coVerify { mockTimeSlotRepositoryRepository.getTimeSlotsForRange(any(), any()) }
    }

    @Test
    fun fetchTimeSlotsForDay_Error() = runTest {
        // Arrange
        val date = LocalDate.of(2024, 11, 5)
        val errorMessage = "Server unavailable"

        coEvery { mockTimeSlotRepositoryRepository.getTimeSlotsForDay(any(), any(), any()) } throws RuntimeException(errorMessage)

        // Act
        viewModel.selectDate(date)

        // Ensure all coroutines complete
        advanceUntilIdle()

        val uiState = viewModel.uiState.value

        // Assert
        assertTrue(uiState.dailyTimeSlots.isEmpty())
        assertEquals("Error: $errorMessage", uiState.dailyErrorMessage)

        
        coVerify { mockTimeSlotRepositoryRepository.getTimeSlotsForDay(any(), any(), any()) }
    }

    @Test
    fun selectedTimeSlot_Success() = runTest {
        // Arrange
        val timeSlot = TimeSlot(id = 1, start = "10:00", end = "11:00", isBookedByUser = false)

        // Act
        viewModel.onTimeSlotSelected(timeSlot)
        val uiState = viewModel.uiState.value

        // Assert
        assertEquals(timeSlot, uiState.selectedTimeSlot)
    }

    @Test
    fun onReserveClicked_Success() = runTest {
        val timeSlot = TimeSlot(id = 1, start = "10:00", end = "11:00", isBookedByUser = false)
        val currentMonth = YearMonth.now()
        viewModel.onTimeSlotSelected(timeSlot)

        coEvery { mockReservationRepository.insertReservation(any()) } returns flow {
            emit(APIResource.Loading<Int>())
            delay(100)
            emit(APIResource.Success(2))
        }

        coEvery { mockTimeSlotRepositoryRepository.getTimeSlotsForRange(
            currentMonth.atDay(1).toString(),
            currentMonth.atEndOfMonth().toString()
        ) } returns TimeSlotResponse(
            start = currentMonth.atDay(1).toString(),
            end = currentMonth.atEndOfMonth().toString(),
            days = emptyList(),
            totalDays = 0
        )

        coEvery { mockTimeSlotRepositoryRepository.getTimeSlotsForDay(any(), any(), any()) } returns emptyList()

        viewModel.onReserveClicked()

        var uiState = viewModel.uiState.first { it.reservationState == ReservationState.PAYMENT_LOADING }
        assertTrue(uiState.showReservationFlow)
        assertTrue(uiState.reservationErrorMessage.isEmpty())

        advanceUntilIdle()

        uiState = viewModel.uiState.value
        assertEquals(ReservationState.CONFIRMATION, uiState.reservationState)
        assertTrue(uiState.reservationErrorMessage.isEmpty())

        
        coVerify { mockReservationRepository.insertReservation(any()) }
        coVerify { mockTimeSlotRepositoryRepository.getTimeSlotsForRange(
            currentMonth.atDay(1).toString(),
            currentMonth.atEndOfMonth().toString()
        ) }
        coVerify(exactly = 0) { mockTimeSlotRepositoryRepository.getTimeSlotsForDay(any(), any(), any()) }
    }

    @Test
    fun onReserveClicked_Error() = runTest {
        val timeSlot = TimeSlot(id = 1, start = "10:00", end = "11:00", isBookedByUser = false)
        val errorMessage = "Er is iets misgelopen"
        viewModel.onTimeSlotSelected(timeSlot)

        coEvery { mockReservationRepository.insertReservation(any()) } returns flow {
            emit(APIResource.Loading<Int>())
            delay(100)
            emit(APIResource.Error(errorMessage))
        }

        viewModel.onReserveClicked()

        var uiState = viewModel.uiState.first { it.reservationState == ReservationState.PAYMENT_LOADING }
        assertTrue(uiState.showReservationFlow)
        assertTrue(uiState.reservationErrorMessage.isEmpty())

        advanceUntilIdle()

        uiState = viewModel.uiState.value
        assertEquals(ReservationState.ERROR, uiState.reservationState)
        assertEquals(errorMessage, uiState.reservationErrorMessage)

        
        coVerify { mockReservationRepository.insertReservation(any()) }
    }

    @Test
    fun onReserveClicked_NoSelectedTimeSlot_DoesNothing() = runTest {
        viewModel.onTimeSlotDismissed()

        viewModel.onReserveClicked()

        val uiState = viewModel.uiState.value
        assertEquals(ReservationState.DETAILS, uiState.reservationState)
        assertFalse(uiState.showReservationFlow)

        
        confirmVerified(mockReservationRepository)
    }

    @Test
    fun clearReservationError_Success() = runTest {
        val timeSlot = TimeSlot(id = 1, start = "10:00", end = "11:00", isBookedByUser = false)
        viewModel.onTimeSlotSelected(timeSlot)

        coEvery { mockReservationRepository.insertReservation(any()) } returns flow {
            emit(APIResource.Error<Int>("Er is iets misgelopen"))
        }

        viewModel.onReserveClicked()
        advanceUntilIdle()

        viewModel.clearReservationError()

        val uiState = viewModel.uiState.value
        assertTrue(uiState.reservationErrorMessage.isEmpty())

        
        coVerify { mockReservationRepository.insertReservation(any()) }
    }

    @Test
    fun onReservationConfirmed_Success() = runTest {
        val timeSlot = TimeSlot(id = 1, start = "10:00", end = "11:00", isBookedByUser = false)
        viewModel.onTimeSlotSelected(timeSlot)

        coEvery { mockReservationRepository.insertReservation(any()) } returns flow {
            emit(APIResource.Success<Int>(2))
        }

        viewModel.onReserveClicked()
        advanceUntilIdle()

        viewModel.onReservationConfirmed()

        val uiState = viewModel.uiState.value
        assertNull(uiState.selectedTimeSlot)
        assertFalse(uiState.showReservationFlow)
        assertEquals(ReservationState.DETAILS, uiState.reservationState)

        
        coVerify { mockReservationRepository.insertReservation(any()) }
    }
}







