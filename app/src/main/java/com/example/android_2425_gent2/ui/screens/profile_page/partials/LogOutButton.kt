package com.example.android_2425_gent2.ui.screens.profile_page.partials

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.android_2425_gent2.R

@Composable
fun LogOutButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
            .padding(16.dp),
        contentPadding = PaddingValues(12.dp),
        colors = ButtonDefaults.buttonColors(
        containerColor = colorResource(R.color.secondary),
        contentColor = colorResource(R.color.secondary_contrast_text)
    )) {
        Icon(
            imageVector = Icons.AutoMirrored.Default.ExitToApp,
            contentDescription = null,
            modifier = modifier.size(24.dp)
        )
        Text(stringResource(R.string.logout))
    }
}