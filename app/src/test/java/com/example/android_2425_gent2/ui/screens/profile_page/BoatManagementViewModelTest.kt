package com.example.android_2425_gent2.ui.screens.profile_page

import com.example.android_2425_gent2.data.network.battery.BatteryDto
import com.example.android_2425_gent2.data.network.boat.BoatDto
import com.example.android_2425_gent2.data.network.users.UserNameDto
import com.example.android_2425_gent2.data.repository.APIResource
import com.example.android_2425_gent2.data.repository.battery.BatteryRepository
import com.example.android_2425_gent2.data.repository.boat.BoatRepository
import com.example.android_2425_gent2.data.repository.user.UserRepository
import com.example.android_2425_gent2.ui.screens.reservations_page.coroutine.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BoatManagementViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: BoatManagementViewModel
    private val mockBatteryRepository: BatteryRepository = mockk()
    private val mockBoatRepository: BoatRepository = mockk()
    private val mockUserRepository: UserRepository = mockk()

    private val sampleBoats = listOf(
        BoatDto(1, "Test Boat 1", "Limba", true),
        BoatDto(2, "Test Boat 2", "Leith", true)
    )

    private val sampleBatteries = listOf(
        BatteryDto(1, "Lithium-Ion", null),
        BatteryDto(2, "Lead-Acid", null)
    )

    private val sampleUsers = listOf(
        UserNameDto(1, "John Doe", "John", "Doe"),
        UserNameDto(2, "Jane Smith", "Jane", "Smith")
    )

    @Before
    fun setup() {
        coEvery { mockBoatRepository.getBoats() } returns flow {
            emit(APIResource.Success(sampleBoats))
        }

        coEvery { mockUserRepository.getUserNames() } returns flow {
            emit(APIResource.Success(sampleUsers))
        }

        coEvery { mockBatteryRepository.getBatteriesByBoat(any()) } returns flow {
            emit(APIResource.Success(sampleBatteries))
        }

        viewModel = BoatManagementViewModel(
            batteryRepository = mockBatteryRepository,
            boatRepository = mockBoatRepository,
            userRepository = mockUserRepository
        )
    }

    @Test
    fun `initial state loads boats successfully`() = runTest {
        advanceUntilIdle()

        with(viewModel.uiState.value) {
            assertEquals(sampleBoats, boats)
            assertFalse(isLoading)
            assertTrue(errorMessage.isEmpty())
            assertNull(selectedBoatId)
        }

        coVerify { mockBoatRepository.getBoats() }
    }

    @Test
    fun `selecting boat loads batteries`() = runTest {
        advanceUntilIdle()
        
        viewModel.selectBoat(1)
        advanceUntilIdle()

        with(viewModel.uiState.value) {
            assertEquals(1, selectedBoatId)
            assertEquals(sampleBatteries, batteries)
            assertFalse(isLoading)
            assertTrue(errorMessage.isEmpty())
        }

        coVerify { 
            mockBoatRepository.getBoats()
            mockBatteryRepository.getBatteriesByBoat(1)
        }
    }

    @Test
    fun `error loading boats updates error state`() = runTest {
        val errorMessage = "Failed to load boats"
        coEvery { mockBoatRepository.getBoats() } returns flow {
            emit(APIResource.Error(errorMessage))
        }

        viewModel = BoatManagementViewModel(
            batteryRepository = mockBatteryRepository,
            boatRepository = mockBoatRepository,
            userRepository = mockUserRepository
        )
        advanceUntilIdle()

        with(viewModel.uiState.value) {
            assertTrue(boats.isEmpty())
            assertFalse(isLoading)
            assertEquals(errorMessage, this.errorMessage)
        }
    }

    @Test
    fun `assign mentor success`() = runTest {
        coEvery { mockBatteryRepository.assignMentor(any(), any()) } returns flow {
            emit(APIResource.Success(Unit))
        }

        advanceUntilIdle()
        viewModel.selectBoat(1)
        advanceUntilIdle()

        viewModel.assignMentor(1, 2)
        advanceUntilIdle()

        with(viewModel.uiState.value) {
            assertFalse(isLoading)
            assertTrue(errorMessage.isEmpty())
            assertEquals("Mentor successfully assigned", successMessage)
        }

        coVerify { 
            mockBatteryRepository.assignMentor(1, 2)
            mockBatteryRepository.getBatteriesByBoat(1)
        }
    }

    @Test
    fun `assign mentor failure`() = runTest {
        val errorMessage = "Failed to assign mentor"
        coEvery { mockBatteryRepository.assignMentor(any(), any()) } returns flow {
            emit(APIResource.Error(errorMessage))
        }

        advanceUntilIdle()
        viewModel.selectBoat(1)
        advanceUntilIdle()

        viewModel.assignMentor(1, 2)
        advanceUntilIdle()

        with(viewModel.uiState.value) {
            assertFalse(isLoading)
            assertEquals(errorMessage, this.errorMessage)
            assertTrue(successMessage.isEmpty())
        }
    }

    @Test
    fun `clear success message`() = runTest {
        advanceUntilIdle()
        
        // Set a success message
        coEvery { mockBatteryRepository.assignMentor(any(), any()) } returns flow {
            emit(APIResource.Success(Unit))
        }
        
        viewModel.selectBoat(1)
        advanceUntilIdle()
        viewModel.assignMentor(1, 2)
        advanceUntilIdle()
        
        // Verify success message exists
        assertEquals("Mentor successfully assigned", viewModel.uiState.value.successMessage)
        
        // Clear the message
        viewModel.clearSuccessMessage()
        
        // Verify message is cleared
        assertTrue(viewModel.uiState.value.successMessage.isEmpty())
    }
}
