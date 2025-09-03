package com.example.android_2425_gent2.ui.screens.calendar_page.partials

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android_2425_gent2.R
import com.example.android_2425_gent2.data.remote.model.TimeSlot
import com.example.android_2425_gent2.utils.DATE_FORMATTER
import com.example.android_2425_gent2.utils.TIME_FORMATTER
import java.time.LocalDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeSlotDetailsBottomSheet(
    timeSlot: TimeSlot,
    onDismiss: () -> Unit,
    onReserveClick: () -> Unit,
    modifier: Modifier = Modifier,
    dayInfo: LocalDate?
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )
    ) {
        Column(
            modifier = modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.reservation_time_slot_details),
                fontSize = 32.sp,
            )
            Box(modifier.height(16.dp))

            Text(
                text = stringResource(R.string.reserve_time_slot) + ": " + dayInfo?.format(DATE_FORMATTER),
                fontSize = 20.sp,
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    timeSlot.start.format(TIME_FORMATTER) + " - " + timeSlot.end.format(TIME_FORMATTER),
                    fontSize = 32.sp
                )

            }

            Spacer(modifier = Modifier.height(32.dp))

            /*no api call to get user info yet*/
            Text("Naam: Phillipe van Achter")
            Text("Tel.: +32 478 85 74 75")
            Text("E-mail: phillipe.van.achter@gmail.com")

            Spacer(modifier = Modifier.height(32.dp))

            PrimaryButtonWithText( onClick = { onReserveClick() }, text=stringResource(R.string.reserve_time_slot) )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}