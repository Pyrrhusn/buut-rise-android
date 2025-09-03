package com.example.android_2425_gent2.ui.screens.profile_page

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android_2425_gent2.MainApplication
import com.example.android_2425_gent2.di.TestContainer
import com.example.android_2425_gent2.ui.theme.Android2425gent2Theme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BatteryManagementScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        val application = ApplicationProvider.getApplicationContext() as MainApplication
        application.container = TestContainer()
        
        composeTestRule.setContent {
            Android2425gent2Theme {
                BatteryManagementScreen(
                    onNavigateBack = {}
                )
            }
        }
    }

    @Test
    fun showsUnassignedBattery() {
        composeTestRule.onNodeWithText("Batterij ID: 2").assertExists()
        composeTestRule.onNodeWithText("Type: Loodzuur").assertExists()
        composeTestRule.onNodeWithText("Huidige Meter/Peter: Geen meter/peter toegewezen").assertExists()
    }


} 