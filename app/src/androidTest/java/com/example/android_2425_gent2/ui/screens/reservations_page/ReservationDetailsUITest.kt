package com.example.android_2425_gent2.ui.screens.reservations_page

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.action.ViewActions.swipeDown
import com.example.android_2425_gent2.MainApplication
import com.example.android_2425_gent2.di.TestContainer
import com.example.android_2425_gent2.ui.theme.Android2425gent2Theme
import com.example.android_2425_gent2.data.network.model.ReservationDetailsDto
import com.example.android_2425_gent2.data.model.OfflineReservation
import com.example.android_2425_gent2.ui.screens.reservations_page.partials.ReservationDetailsBottomModalSheet
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class ReservationDetailsUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockReservation = OfflineReservation(
        start = LocalTime.of(10, 0),
        end = LocalTime.of(12, 0),
        date = LocalDate.now(),
        boatId = 1,
        boatPersonalName = "Test Boat",
        id = 1
    )

    private val mockDetails = ReservationDetailsDto(
        id = 1,
        start = "10:00",
        end = "12:00",
        date = LocalDate.now().toString(),
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

    private var isLoading = false
    private var details: ReservationDetailsDto? = mockDetails

    @Before
    fun setup() {
        val application = ApplicationProvider.getApplicationContext() as MainApplication
        application.container = TestContainer()
    }

    private fun setContent() {
        composeTestRule.setContent {
            Android2425gent2Theme {
                ReservationDetailsBottomModalSheet(
                    selectedReservation = mockReservation,
                    reservationDetails = details,
                    isLoading = isLoading,
                    onSelectedReservationChange = {},
                    modifier = Modifier,
                    onCancelReservation = {  }
                )
            }
        }
    }

    @Test
    fun showsReservationDetails() {
        setContent()
        composeTestRule.onNodeWithText("Gegevens ophaal persoon").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("10:00 - 12:00").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("Naam: John Doe").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("Tel.: 123456789").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("E-mail: john@example.com").assertExists().assertIsDisplayed()
    }

    @Test
    fun showsLoadingState() {
        isLoading = true
        details = null
        
        setContent()

        composeTestRule.onNodeWithTag("LoadingIndicator")
            .assertExists()
            .assertIsDisplayed()
    }


    @Test
    fun showsNoInformationMessage_whenDetailsNotAvailable() {
        details = ReservationDetailsDto(
            id = 1,
            start = "10:00",
            end = "12:00",
            date = LocalDate.now().toString(),
            isDeleted = false,
            boatPersonalName = "Test Boat",
            mentorName = null,
            batteryId = 1,
            currentBatteryUserName = null,
            currentBatteryUserId = 1,
            currentHolderPhoneNumber = null,
            currentHolderEmail = null,
            currentHolderStreet = null,
            currentHolderNumber = null,
            currentHolderCity = null,
            currentHolderPostalCode = null
        )
        
        setContent()
        
        composeTestRule.onNodeWithText("Geen ophaal informatie beschikbaar").assertExists().assertIsDisplayed()
    }
}
