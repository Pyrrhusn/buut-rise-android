package com.example.android_2425_gent2.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.auth0.android.result.Credentials
import com.example.android_2425_gent2.MainApplication
import com.example.android_2425_gent2.ui.screens.app_page.AppViewModel
import com.example.android_2425_gent2.ui.screens.calendar_page.CalendarViewModel
import com.example.android_2425_gent2.ui.screens.login_page.LoginViewModel
import com.example.android_2425_gent2.ui.screens.notification_page.NotificationDetailsViewModel
import com.example.android_2425_gent2.ui.screens.notification_page.NotificationViewModel
import com.example.android_2425_gent2.ui.screens.profile_page.GuestUsersViewModel
import com.example.android_2425_gent2.ui.screens.profile_page.UserDetailsViewModel
import com.example.android_2425_gent2.ui.screens.profile_page.ProfilePageViewModel
import com.example.android_2425_gent2.ui.screens.reservations_page.ReservationsViewModel
import com.example.android_2425_gent2.ui.screens.profile_page.BoatManagementViewModel
import com.example.android_2425_gent2.ui.screens.profile_page.BatteryManagementViewModel

object AppViewModelProvider {
    val LOGIN_KEY = object : CreationExtras.Key<(Credentials) -> Unit> {}
    val Factory = viewModelFactory {
        initializer {
            ReservationsViewModel(mainApplication().container.reservationRepository)
        }
        initializer {
            CalendarViewModel(
                mainApplication().container.timeSlotRepository,
                mainApplication().container.reservationRepository
            )
        }
        initializer {
            LoginViewModel(
                login = login(),
                authRepo = mainApplication().container.authRepo
            )
        }
        initializer {
            AppViewModel(mainApplication().container.authRepo)
        }
        initializer {
            NotificationDetailsViewModel(
                mainApplication().container.notificationRepository
            )
        }
        initializer {
            NotificationViewModel(mainApplication().container.notificationRepository)
        }

        initializer {
            ProfilePageViewModel(
                authRepo = mainApplication().container.authRepo,
                userRepository = mainApplication().container.userRepository
            )
        }

        initializer {
            GuestUsersViewModel(
                mainApplication().container.userRepository
            )
        }

        initializer {
            UserDetailsViewModel(
                mainApplication().container.userRepository
            )
        }

        initializer {
            BoatManagementViewModel(
                mainApplication().container.batteryRepository,
                mainApplication().container.boatRepository,
                mainApplication().container.userRepository
            )
        }

        initializer {
            BatteryManagementViewModel(
                mainApplication().container.batteryRepository,
                mainApplication().container.userRepository
            )
        }

    }

}

fun CreationExtras.mainApplication(): MainApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MainApplication)

fun CreationExtras.login(): (Credentials) -> Unit =
    (this[AppViewModelProvider.LOGIN_KEY] as (Credentials) -> Unit)