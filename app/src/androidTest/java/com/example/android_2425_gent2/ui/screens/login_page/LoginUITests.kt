package com.example.android_2425_gent2.ui.screens.login_page

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import com.example.android_2425_gent2.MainApplication
import com.example.android_2425_gent2.data.repository.auth.TestAuth0Repo
import com.example.android_2425_gent2.di.AppContainer
import com.example.android_2425_gent2.di.TestContainer
import com.example.android_2425_gent2.ui.theme.Android2425gent2Theme
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginUITests {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val loaderTestId = "LoadingIndicator"

    private fun getContainer(): AppContainer{
        val application = ApplicationProvider.getApplicationContext() as MainApplication
        return application.container
    }

    private fun getTestAuth0Repo(): TestAuth0Repo {
        return getContainer().authRepo as TestAuth0Repo
    }

    @Before
    fun setContainer() {
        val application = ApplicationProvider.getApplicationContext() as MainApplication
        application.container = TestContainer()

        composeTestRule.setContent {
            Android2425gent2Theme {
                LoginPage(login = {})
            }
        }
    }

    private fun inputEmail(email: String){
        composeTestRule.onNodeWithTag("EmailField").performClick()
        composeTestRule.onNodeWithTag("EmailField")
            .performTextInput(email)
    }

    private fun inputPassword(password: String) {
        composeTestRule.onNodeWithTag("PasswordField").performClick()
        composeTestRule.onNodeWithTag("PasswordField")
            .performTextInput(password)
    }

    private fun inputLogin(email: String = "example@email.com", password: String = "password123"){
        inputEmail(email)
        inputPassword(password)
        composeTestRule.onNodeWithTag("LoginButton").performClick()
    }

    private fun checkVisibleErrorMessage(errorMessage: String){
        composeTestRule.onNodeWithTag("ErrorText").assertExists()
        composeTestRule.onNodeWithTag("ErrorText").isDisplayed()
        composeTestRule.onNodeWithTag("ErrorText")
            .assertTextEquals(errorMessage)
    }

    private fun checkVisibleLoader(){
        composeTestRule.onNodeWithTag(loaderTestId).assertExists()
        composeTestRule.onNodeWithTag(loaderTestId).isDisplayed()
    }

    @Test
    fun showsEmailField() {
        composeTestRule.onNodeWithTag("EmailField").assertExists()
        composeTestRule.onNodeWithTag("EmailField").isDisplayed()
    }

    @Test
    fun showsPasswordField() {
        composeTestRule.onNodeWithTag("PasswordField").assertExists()
        composeTestRule.onNodeWithTag("PasswordField").isDisplayed()
    }

    @Test
    fun clickLoginButton_Login() {
        val authRepo:TestAuth0Repo = getTestAuth0Repo()
        inputLogin()

        authRepo.triggerLoading()
        composeTestRule.onNodeWithTag("LoadingIndicator").assertExists()
        composeTestRule.onNodeWithTag("LoadingIndicator").isDisplayed()

        authRepo.triggerLoginSuccess()
        composeTestRule.onNodeWithTag("LoadingIndicator").isNotDisplayed()
    }

    @Test
    fun wrongEmailFormat_ShowsErrorMessage() {
        inputEmail("exampleemail.com")

        checkVisibleErrorMessage("Invalid email format")
    }

    @Test
    fun shortPassword_ShowsErrorMessage() {
        inputPassword("1234567")

        checkVisibleErrorMessage("Password must be at least 8 characters")
    }

    @Test
    fun longPassword_ShowsErrorMessage() {
        inputPassword("123456789012345678901234567890123456789012345678901234567890123456789012")

        checkVisibleErrorMessage("Password must be less than or equal to 72 characters")
    }

    @Test
    fun clickLoginButtonWithWrongLogin_showsErrorMessage() = runTest {
        val authRepo:TestAuth0Repo = getTestAuth0Repo()
        val errorMessage = "Authentication failed: Invalid email or password"
        inputLogin()

        authRepo.triggerLoading()

        checkVisibleLoader()

        authRepo.triggerLoginFailure(errorMessage)

        composeTestRule.onNodeWithTag(loaderTestId).isNotDisplayed()

        checkVisibleErrorMessage(errorMessage)
    }
}