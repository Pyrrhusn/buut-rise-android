package com.example.android_2425_gent2.ui.screens.app_page;

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.core.app.ApplicationProvider
import com.example.android_2425_gent2.MainApplication
import com.example.android_2425_gent2.data.repository.auth.TestAuth0Repo
import com.example.android_2425_gent2.di.TestContainer
import com.example.android_2425_gent2.ui.theme.Android2425gent2Theme
import org.junit.Rule
import org.junit.Test

class AppUITest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private fun setContainer(loggedIn: Boolean = false) {
        val application = ApplicationProvider.getApplicationContext() as MainApplication
        val testContainer = TestContainer()
        application.container = testContainer

        if (loggedIn) {
            val testAuth0Repo: TestAuth0Repo = testContainer.authRepo as TestAuth0Repo
            testAuth0Repo.login()
        }

        composeTestRule.setContent {
            Android2425gent2Theme {
                App(windowSize = WindowWidthSizeClass.Compact)
            }
        }
    }

    @Test
    fun showsLoginPageWhenNotLoggedIn() {
        setContainer()
        composeTestRule.onNodeWithTag("LoginPage").assertExists()
        composeTestRule.onNodeWithTag("LoginPage").isDisplayed()
        composeTestRule.onNodeWithTag("MainScreen").assertDoesNotExist()
    }

    @Test
    fun showsMainScreenWhenLoggedIn() {
        setContainer(loggedIn = true)
        composeTestRule.onNodeWithTag("MainScreen").assertExists()
        composeTestRule.onNodeWithTag("MainScreen").isDisplayed()
        composeTestRule.onNodeWithTag("LoginPage").assertDoesNotExist()
    }
}
