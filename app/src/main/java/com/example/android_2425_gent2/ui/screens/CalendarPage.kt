package com.example.android_2425_gent2.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android_2425_gent2.ui.screens.calendar_page.CalendarView
import com.example.android_2425_gent2.ui.screens.reservations_page.ReservationsPage
import java.time.LocalDate

@Composable
fun CalendarPage(modifier: Modifier = Modifier) {
    var selectedTab: ReservationTab by rememberSaveable { mutableStateOf(ReservationTab.RESERVE) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    Column(modifier = modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = selectedTab.tabIndex,
            containerColor = Color.White,
            contentColor = Color.DarkGray,
            divider = {}, // Remove the default divider
            indicator = { tabPositions ->
                SecondaryIndicator(
                    Modifier
                        .tabIndicatorOffset(tabPositions[selectedTab.tabIndex])
                        .padding(horizontal = 24.dp)
                        .height(4.dp),
                    color = Color.Black
                )
            }
        ) {
            val reserveSelected: Boolean = selectedTab == ReservationTab.RESERVE
            Tab(
                selected = selectedTab == ReservationTab.RESERVE,
                onClick = { selectedTab = ReservationTab.RESERVE },
                text = {
                    Text(
                        "Reserveer",
                        fontSize = 16.sp,
                        fontWeight = if (reserveSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (reserveSelected) Color.Black else Color.Gray
                    )
                },
                modifier = Modifier.padding(8.dp)
            )
            val reservationsSelected: Boolean = selectedTab == ReservationTab.RESERVATIONS
            Tab(
                selected = reservationsSelected,
                onClick = { selectedTab = ReservationTab.RESERVATIONS },
                text = {
                    Text(
                        "Uw reservaties",
                        fontSize = 16.sp,
                        fontWeight = if (reservationsSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (reservationsSelected) Color.Black else Color.Gray
                    )
                },
                modifier = Modifier.padding(8.dp)
            )
        }

        when (selectedTab) {
            ReservationTab.RESERVE -> CalendarView()
            ReservationTab.RESERVATIONS -> ReservationsPage() // Placeholder voor de reservaties tab
        }
    }
}

enum class ReservationTab(val tabIndex: Int) {
    RESERVE(tabIndex = 0),
    RESERVATIONS(tabIndex = 1)
}