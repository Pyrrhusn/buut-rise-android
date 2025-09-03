package com.example.android_2425_gent2.ui.screens.reservations_page.partials

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.android_2425_gent2.R

@Composable
fun CancelReservationButton(
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error,
            disabledContainerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.12f)
        ),
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.cancel_reservation),
            color = if (enabled) {
                MaterialTheme.colorScheme.onError
            } else {
                MaterialTheme.colorScheme.onError.copy(alpha = 0.38f)
            }
        )
    }
} 