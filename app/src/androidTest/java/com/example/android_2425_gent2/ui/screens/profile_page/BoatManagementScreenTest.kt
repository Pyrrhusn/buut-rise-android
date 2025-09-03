package com.example.android_2425_gent2.ui.screens.profile_page

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android_2425_gent2.MainApplication
import com.example.android_2425_gent2.data.repository.boat.TestBoatRepository
import com.example.android_2425_gent2.di.TestContainer
import com.example.android_2425_gent2.ui.theme.Android2425gent2Theme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BoatManagementScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private var navigateBackCalled = false

    @Before
    fun setup() {
        val application = ApplicationProvider.getApplicationContext() as MainApplication
        application.container = TestContainer()
        
        composeTestRule.setContent {
            Android2425gent2Theme {
                BoatManagementScreen(
                    onNavigateBack = { navigateBackCalled = true }
                )
            }
        }
    }

    @Test
    fun showsBoatList() {
        composeTestRule.onNodeWithText("Limba").assertExists()
        composeTestRule.onNodeWithText("Test Boat 1").assertExists()
    }

    @Test
    fun selectBoatShowsBatteries() {
        // Click on first boat
        composeTestRule.onNodeWithText("Limba").performClick()
        
        // Verify batteries are shown
        composeTestRule.onNodeWithText("Batterij ID: 1").assertExists()
        composeTestRule.onNodeWithText("Type: Lithium-Ion").assertExists()
    }

    @Test
    fun navigateBackWorks() {
        navigateBackCalled = false
        composeTestRule.onNodeWithContentDescription("Back").performClick()
        assert(navigateBackCalled)
    }
} 