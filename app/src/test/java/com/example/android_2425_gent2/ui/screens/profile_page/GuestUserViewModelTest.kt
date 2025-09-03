package com.example.android_2425_gent2.ui.screens.profile_page

import com.example.android_2425_gent2.data.model.UserSurface
import com.example.android_2425_gent2.data.repository.APIResource
import com.example.android_2425_gent2.data.repository.user.UserRepository
import com.example.android_2425_gent2.ui.screens.reservations_page.coroutine.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GuestUsersViewModelTest {

    @get:Rule
    val coroutineRule = MainDispatcherRule()

    private lateinit var viewModel: GuestUsersViewModel
    private val mockUserRepository: UserRepository = mockk()

    @Before
    fun setup() {
        viewModel = GuestUsersViewModel(mockUserRepository)
    }

    @Test
    fun `fetchUsers loads users successfully`() = runTest {
        val testUsers = listOf(
            UserSurface(id = 1, familyName = "Test User 1"),
            UserSurface(id = 2, familyName = "Test User 2")
        )

        coEvery { mockUserRepository.getUsers() } returns flow {
            emit(APIResource.Loading())
            emit(APIResource.Success(testUsers))
        }

        viewModel.fetchUsers()
        advanceUntilIdle()

        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertEquals(testUsers, uiState.users)
        assertTrue(uiState.errorMessage.isEmpty())

        coVerify { mockUserRepository.getUsers() }
    }

    @Test
    fun `fetchUsers handles error state`() = runTest {
        val errorMessage = "Network error"

        coEvery { mockUserRepository.getUsers() } returns flow {
            emit(APIResource.Loading())
            emit(APIResource.Error(errorMessage))
        }

        viewModel.fetchUsers()
        advanceUntilIdle()

        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertTrue(uiState.users.isEmpty())
        assertEquals(errorMessage, uiState.errorMessage)

        coVerify { mockUserRepository.getUsers() }
    }

    @Test
    fun `fetchUsers handles null data in success state`() = runTest {
        coEvery { mockUserRepository.getUsers() } returns flow {
            emit(APIResource.Loading())
            emit(APIResource.Success(null))
        }

        viewModel.fetchUsers()
        advanceUntilIdle()

        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertTrue(uiState.users.isEmpty())
        assertTrue(uiState.errorMessage.isEmpty())

        coVerify { mockUserRepository.getUsers() }
    }
}