package com.example.android_2425_gent2.ui.screens.profile_page

import com.example.android_2425_gent2.data.network.battery.BatteryDto
import com.example.android_2425_gent2.data.network.battery.Mentor
import com.example.android_2425_gent2.data.network.users.UserNameDto
import com.example.android_2425_gent2.data.repository.APIResource
import com.example.android_2425_gent2.data.repository.battery.BatteryRepository
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
class BatteryManagementViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: BatteryManagementViewModel
    private val mockBatteryRepository: BatteryRepository = mockk()
    private val mockUserRepository: UserRepository = mockk()

    private val sampleBatteries = listOf(
        BatteryDto(
            id = 1,
            type = "Lithium-Ion",
            mentor = Mentor(1, "John Doe", "John", "Doe")
        ),
        BatteryDto(
            id = 2,
            type = "Loodzuur",
            mentor = null
        )
    )

    private val sampleUsers = listOf(
        UserNameDto(1, "John Doe", "John", "Doe"),
        UserNameDto(2, "Jane Smith", "Jane", "Smith")
    )

    @Before
    fun setup() {
        coEvery { mockBatteryRepository.getAllBatteries() } returns flow {
            emit(APIResource.Success(sampleBatteries))
        }
        
        coEvery { mockUserRepository.getUserNames() } returns flow {
            emit(APIResource.Success(sampleUsers))
        }
    }

    @Test
    fun testInitialStateLoadsSuccessfully() = runTest {
        viewModel = BatteryManagementViewModel(mockBatteryRepository, mockUserRepository)
        advanceUntilIdle()

        with(viewModel.uiState.value) {
            assertEquals(sampleBatteries, batteries)
            assertEquals(sampleUsers, users)
            assertFalse(isLoading)
            assertTrue(errorMessage.isEmpty())
        }

        coVerify { 
            mockBatteryRepository.getAllBatteries()
            mockUserRepository.getUserNames()
        }
    }

    @Test
    fun testErrorStateOnBatteryFetchFailure() = runTest {
        val errorMessage = "Failed to load batteries"
        coEvery { mockBatteryRepository.getAllBatteries() } returns flow {
            emit(APIResource.Error(errorMessage))
        }

        viewModel = BatteryManagementViewModel(mockBatteryRepository, mockUserRepository)
        advanceUntilIdle()

        with(viewModel.uiState.value) {
            assertFalse(isLoading)
            assertEquals(errorMessage, this.errorMessage)
            assertTrue(batteries.isEmpty())
        }
    }

    @Test
    fun testAssignMentorSuccess() = runTest {
        coEvery { mockBatteryRepository.assignMentor(any(), any()) } returns flow {
            emit(APIResource.Success(Unit))
        }
        
        coEvery { mockBatteryRepository.getAllBatteries() } returns flow {
            emit(APIResource.Success(sampleBatteries))
        }

        viewModel = BatteryManagementViewModel(mockBatteryRepository, mockUserRepository)
        advanceUntilIdle()

        viewModel.assignMentor(1, 2)
        advanceUntilIdle()

        with(viewModel.uiState.value) {
            assertFalse(isLoading)
            assertTrue(errorMessage.isEmpty())
            assertEquals("Meter/Peter succesvol toegewezen", successMessage)
        }

        coVerify { 
            mockBatteryRepository.assignMentor(1, 2)
            mockBatteryRepository.getAllBatteries()
        }
    }

    @Test
    fun testAssignMentorFailure() = runTest {
        val errorMessage = "Failed to assign mentor"
        coEvery { mockBatteryRepository.assignMentor(any(), any()) } returns flow {
            emit(APIResource.Error(errorMessage))
        }

        viewModel = BatteryManagementViewModel(mockBatteryRepository, mockUserRepository)
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
    fun testClearSuccessMessage() = runTest {
        coEvery { mockBatteryRepository.assignMentor(any(), any()) } returns flow {
            emit(APIResource.Success(Unit))
        }

        viewModel = BatteryManagementViewModel(mockBatteryRepository, mockUserRepository)
        advanceUntilIdle()

        viewModel.assignMentor(1, 2)
        advanceUntilIdle()
        
        assertEquals("Meter/Peter succesvol toegewezen", viewModel.uiState.value.successMessage)
        
        viewModel.clearSuccessMessage()
        assertTrue(viewModel.uiState.value.successMessage.isEmpty())
    }
} 