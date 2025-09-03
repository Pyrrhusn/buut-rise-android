package com.example.android_2425_gent2.ui.common

import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.android_2425_gent2.R
import androidx.compose.ui.platform.testTag

@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier,
    color: Color = colorResource(R.color.primary),
    trackColor: Color = colorResource(R.color.primary_darken)
) {
    CircularProgressIndicator(
        modifier = modifier.width(64.dp).testTag("loadingIndicator"),
        color = color,
        trackColor = trackColor
    )
}