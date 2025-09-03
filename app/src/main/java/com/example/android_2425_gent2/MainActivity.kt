package com.example.android_2425_gent2

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.android_2425_gent2.ui.screens.app_page.App
import com.example.android_2425_gent2.ui.theme.Android2425gent2Theme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val windowSize = calculateWindowSizeClass(this)

            Android2425gent2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    App(windowSize = windowSize.widthSizeClass)
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    Android2425gent2Theme {
        Scaffold(modifier = Modifier.fillMaxSize()) {
            // For preview, we use Compact as default
            App(windowSize = androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Compact)
        }
    }
}