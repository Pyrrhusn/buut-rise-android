package com.example.android_2425_gent2.ui.screens.guest_users

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.ApplicationProvider
import com.example.android_2425_gent2.MainApplication
import com.example.android_2425_gent2.data.model.UserSurface
import com.example.android_2425_gent2.data.repository.user.TestUserRepository
import com.example.android_2425_gent2.di.AppContainer
import com.example.android_2425_gent2.di.TestContainer
import com.example.android_2425_gent2.ui.screens.profile_page.GuestUsersScreen
import com.example.android_2425_gent2.ui.theme.Android2425gent2Theme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GuestUsersUITests {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun getContainer(): AppContainer {
        val application = ApplicationProvider.getApplicationContext() as MainApplication
        return application.container
    }

    private fun getTestUserRepo(): TestUserRepository {
        return getContainer().userRepository as TestUserRepository
    }

    @Before
    fun setContainer() {
        val application = ApplicationProvider.getApplicationContext() as MainApplication
        application.container = TestContainer()

        composeTestRule.setContent {
            Android2425gent2Theme {
                GuestUsersScreen(
                    onNavigateToUserDetails = {},
                    onNavigateBack = {}
                )
            }
        }
    }

    private fun checkErrorMessage(errorMessage: String) {
        composeTestRule.onNodeWithText(errorMessage).assertExists()
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }

    private fun checkUsersList(users: List<UserSurface>) {
        users.forEach { user ->
            composeTestRule.onNodeWithText(user.familyName).assertExists()
            composeTestRule.onNodeWithText(user.familyName).assertIsDisplayed()
        }
    }


    @Test
    fun showsErrorMessage() {
        val userRepo = getTestUserRepo()
        val errorMessage = "Failed to load users"

        userRepo.triggerError(errorMessage)
        composeTestRule.waitForIdle()

        checkErrorMessage(errorMessage)
    }

    @Test
    fun showsUsersList() {
        val userRepo = getTestUserRepo()
        val testUsers = listOf(
            UserSurface(id = 1, familyName = "Test User 1"),
            UserSurface(id = 2, familyName = "Test User 2")
        )

        userRepo.triggerSuccessWithCustomUsers(testUsers)
        composeTestRule.waitForIdle()
        checkUsersList(testUsers)
    }

    @Test
    fun loadingToSuccess_ShowsUsersList() {
        val userRepo = getTestUserRepo()

        userRepo.triggerLoading()

        val testUsers = listOf(
            UserSurface(id = 1, familyName = "Test User 1"),
            UserSurface(id = 2, familyName = "Test User 2")
        )

        userRepo.triggerSuccessWithCustomUsers(testUsers)
        composeTestRule.waitForIdle()
        checkUsersList(testUsers)
    }

    @Test
    fun loadingToError_ShowsErrorMessage() {
        val userRepo = getTestUserRepo()
        val errorMessage = "Failed to load users"

        userRepo.triggerLoading()

        userRepo.triggerError(errorMessage)
        composeTestRule.waitForIdle()
        checkErrorMessage(errorMessage)
    }
}