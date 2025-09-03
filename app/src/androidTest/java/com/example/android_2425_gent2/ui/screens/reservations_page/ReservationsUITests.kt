package com.example.android_2425_gent2.ui.screens.reservations_page


import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasRequestFocusAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import com.example.android_2425_gent2.MainApplication
import com.example.android_2425_gent2.di.TestContainer
import com.example.android_2425_gent2.ui.theme.Android2425gent2Theme
import com.example.android_2425_gent2.utils.DATE_FORMATTER
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class ReservationsUITests {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockDate = LocalDate.now()

    @Before
    fun setContainer() {
        val application = ApplicationProvider.getApplicationContext() as MainApplication
        application.container = TestContainer()

        composeTestRule.setContent {
            Android2425gent2Theme {
                ReservationsPage()
            }
        }
    }

    @Test
    fun selectOldReservations_showsOldReservations() {
        composeTestRule.onNodeWithTag("ReservationTypeSelectionDropDownMenu").performClick()
        composeTestRule.onNodeWithText("Oude reservaties").performClick()
        
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithText("11:30 - 13:30")
                .fetchSemanticsNodes().isNotEmpty()
        }
        
        composeTestRule.onNodeWithText("11:30 - 13:30", useUnmergedTree = true).assertExists()
        composeTestRule.onNodeWithText("14:00 - 16:00", useUnmergedTree = true).assertExists()
    }


    @Test
    fun selectUpcomingReservations_showsUpcomingReservations() {
        composeTestRule.onNodeWithTag("ReservationTypeSelectionDropDownMenu").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("ReservationType.UPCOMING").performClick()
        
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithText("11:30 - 13:30")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("11:30 - 13:30", useUnmergedTree = true).assertExists()
        composeTestRule.onNodeWithText("14:00 - 16:00", useUnmergedTree = true).assertExists()
    }
}