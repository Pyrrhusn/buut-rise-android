package com.example.android_2425_gent2.ui.screens.calendar_page


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android_2425_gent2.data.remote.model.TimeSlot
import com.example.android_2425_gent2.ui.AppViewModelProvider
import com.example.android_2425_gent2.ui.common.ErrorMessage
import com.example.android_2425_gent2.ui.screens.calendar_page.partials.MonthCalendar
import com.example.android_2425_gent2.ui.screens.calendar_page.partials.MonthSelector
import com.example.android_2425_gent2.ui.screens.calendar_page.partials.TimeSlotDetailsBottomSheet
import com.example.android_2425_gent2.ui.screens.calendar_page.partials.TimeSlotReservationConfirmationSheet
import com.example.android_2425_gent2.ui.screens.calendar_page.partials.TimeSlotView
import java.time.LocalDate


@Composable
fun CalendarView(
    viewModel: CalendarViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        MonthSelector(
            currentMonth = uiState.currentMonth,
            onMonthChange = { viewModel.changeMonth(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        CalendarContent(
            uiState = uiState,
            onDateSelected = { viewModel.selectDate(it) }
        )

        if (uiState.selectedDate != null) {
            Spacer(modifier = Modifier.height(16.dp))
            TimeSlotContent(
                uiState = uiState,
                onTimeSlotClick = { viewModel.onTimeSlotSelected(it) }
            )
        }
    }

    uiState.selectedTimeSlot?.let { timeSlot ->
        if (!uiState.showReservationFlow) {
            TimeSlotDetailsBottomSheet(
                dayInfo = uiState.selectedDate,
                timeSlot = timeSlot,
                onDismiss = { viewModel.onTimeSlotDismissed() },
                onReserveClick = { viewModel.onReserveClicked() }
            )
        } else {
            TimeSlotReservationConfirmationSheet(
                dayInfo = uiState.selectedDate,
                timeSlot = timeSlot,
                reservationErrorMessage = uiState.reservationErrorMessage,
                reservationState = uiState.reservationState,
                onDismiss = { viewModel.onReservationConfirmed() }
            )
        }
    }

}



@Composable
private fun CalendarContent(
    uiState: CalendarUiState,
    onDateSelected: (LocalDate) -> Unit
) {
    when {
        uiState.isLoadingMonth -> LoadingIndicator()
        uiState.monthErrorMessage.isNotEmpty() -> ErrorMessage(uiState.monthErrorMessage)
        else -> MonthCalendar(
            currentMonth = uiState.currentMonth,
            selectedDate = uiState.selectedDate,
            onDateSelected = onDateSelected,
            availableDays = uiState.monthTimeSlots
        )
    }
}


@Composable
private fun TimeSlotContent(uiState: CalendarUiState, onTimeSlotClick: (TimeSlot) -> Unit) {
    when {
        uiState.isLoadingDaily -> LoadingIndicator()
        uiState.dailyErrorMessage.isNotEmpty() -> ErrorMessage(uiState.dailyErrorMessage)
        else -> TimeSlotView(
            timeSlots = uiState.dailyTimeSlots,
            onTimeSlotClick = onTimeSlotClick

        )
    }
}


@Composable
private fun LoadingIndicator() {
    CircularProgressIndicator(
        modifier = Modifier.size(24.dp)
    )
}

@Composable
private fun ErrorMessage(message: String) {
    Text(
        text = message,
        color = MaterialTheme.colorScheme.error
    )
}

