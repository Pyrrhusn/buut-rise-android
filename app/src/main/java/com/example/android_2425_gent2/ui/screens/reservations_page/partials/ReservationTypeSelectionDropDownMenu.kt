package com.example.android_2425_gent2.ui.screens.reservations_page.partials

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.example.android_2425_gent2.ui.screens.reservations_page.ReservationType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationTypeSelectionDropDownMenu(
    reservationType: ReservationType,
    onReservationTypeChange: (ReservationType) -> Unit,
    modifier: Modifier
) {
    var expanded: Boolean by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        TextField(
            value = stringResource(reservationType.title),
            readOnly = true,
            onValueChange = {},
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = modifier
                .menuAnchor()
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = !expanded
            },
            modifier = modifier
                .exposedDropdownSize(true)
        ) {
            DropdownMenuItem(
                text = {
                    Text(stringResource(ReservationType.UPCOMING.title))
                },
                onClick = {
                    onReservationTypeChange(ReservationType.UPCOMING)
                    expanded = !expanded
                },
                modifier = Modifier.testTag("ReservationType.UPCOMING")
            )
            DropdownMenuItem(
                text = {
                    Text(stringResource(ReservationType.OLD.title))
                },
                onClick = {
                    onReservationTypeChange(ReservationType.OLD)
                    expanded = !expanded
                },
            )
            DropdownMenuItem(
                text = {
                    Text(stringResource(ReservationType.CANCELED.title))
                },
                onClick = {
                    onReservationTypeChange(ReservationType.CANCELED)
                    expanded = !expanded
                }
            )
        }
    }
}