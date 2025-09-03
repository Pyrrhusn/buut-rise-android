package com.example.android_2425_gent2.ui.screens.login_page

import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.auth0.android.result.Credentials
import com.example.android_2425_gent2.R
import com.example.android_2425_gent2.ui.AppViewModelProvider
import com.example.android_2425_gent2.ui.common.LoadingIndicator

@Composable
fun LoginPage(
    login: (Credentials) -> Unit, modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val extras = MutableCreationExtras().apply {
        set(AppViewModelProvider.LOGIN_KEY, login)
        set(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY, LocalContext.current.applicationContext as Application)
    }
    val viewModel: LoginViewModel = viewModel(
        factory = AppViewModelProvider.Factory,
        extras = extras,
    )

    val credentialsState = viewModel.credentialsState
    val uiState = viewModel.uiState

    // Observe URL opening events
    LaunchedEffect(viewModel.openUrlEvent.value) {
        val url = viewModel.openUrlEvent.value
        if (url != null) {
            try {
                val webpage = Uri.parse(url)
                val intent = Intent(Intent.ACTION_VIEW, webpage)
                context.startActivity(intent)
                viewModel.onUrlOpened()
            } catch (e: ActivityNotFoundException) {
                viewModel.setError("Something went wrong while opening the link. Make sure you have a browser installed.")
            }
        }
    }

    LoginPageContent(
        email = credentialsState.email,
        password = credentialsState.password,
        isLoading = uiState.isLoading,
        error = uiState.error,
        loginDisabled = uiState.disableLogin,
        onUsernameChange = { viewModel.setEmail(it); viewModel.onAnyInputChanged() },
        onPasswordChange = { viewModel.setPassword(it); viewModel.onAnyInputChanged() },
        onLoginClick = { viewModel.handleLogin() },
        onRegisterClick = { viewModel.handleRegister() },
        modifier = modifier
    )
}

@Composable
private fun LoginPageContent(
    email: String,
    password: String,
    isLoading: Boolean,
    error: String?,
    loginDisabled: Boolean,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.primary))
            .padding(horizontal = 64.dp)
            .testTag("LoginPage"),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.buut_logo_white),
                contentDescription = "App Logo",
                modifier = modifier.size(100.dp)
            )
            if(!isLoading) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Login",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = onUsernameChange,
                        label = { Text("Email") },
                        modifier = modifier.fillMaxWidth().testTag("EmailField"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.White,
                            cursorColor = Color.White
                        )
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = onPasswordChange,
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        modifier = modifier.fillMaxWidth().testTag("PasswordField"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.White,
                            cursorColor = Color.White
                        )
                    )
                    if(error != null) Text(error, modifier = modifier.testTag("ErrorText"), color = Color.Red)
                }

                Spacer(modifier = modifier.height(16.dp))
                Column(
                    modifier = modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onLoginClick,
                        modifier = modifier.fillMaxWidth().testTag("LoginButton"),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.White
                        ),
                        border = BorderStroke(1.dp, Color.White),
                        enabled = !loginDisabled
                    ) {
                        Text("Login")
                    }

                    TextButton(
                        onClick = onRegisterClick,
                        modifier = modifier.fillMaxWidth(),
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color.White
                        )
                    ) {
                        Text("Register")
                    }
                }

                Spacer(modifier = modifier.height(48.dp))
            } else {
                LoadingIndicator(modifier.testTag("LoadingIndicator"), colorResource(R.color.secondary))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginPagePreview() {
    LoginPageContent(
        email = "",
        password = "",
        isLoading = false,
        error = null,
        loginDisabled = false,
        onUsernameChange = {},
        onPasswordChange = {},
        onLoginClick = {},
        onRegisterClick = {}
    )
}