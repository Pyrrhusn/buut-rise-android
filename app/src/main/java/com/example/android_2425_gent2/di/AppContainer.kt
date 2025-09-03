package com.example.android_2425_gent2.di

import com.example.android_2425_gent2.data.repository.battery.NetworkBatteryRepository
import android.content.Context
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.storage.SecureCredentialsManager
import com.auth0.android.authentication.storage.SharedPreferencesStorage
import com.example.android_2425_gent2.data.local.AppDatabase
import com.example.android_2425_gent2.data.repository.auth.Auth0Repo
import com.example.android_2425_gent2.data.repository.auth.IAuthRepo
import com.example.android_2425_gent2.data.repository.auth.TestAuth0Repo
import com.example.android_2425_gent2.data.repository.notification.NotificationRepository
import com.example.android_2425_gent2.data.repository.notification.OfflineFirstNotificationRepository
import com.example.android_2425_gent2.data.repository.notification.TestNotificationRepository
import com.example.android_2425_gent2.data.repository.reservation.OfflineFirstReservationRepository
import com.example.android_2425_gent2.data.repository.reservation.ReservationRepository
import com.example.android_2425_gent2.data.repository.reservation.TestReservationRepository
import com.example.android_2425_gent2.data.repository.timeslot.NetworkTimeSlotRepository
import com.example.android_2425_gent2.data.repository.timeslot.TestTimeSlotRepository
import com.example.android_2425_gent2.data.repository.timeslot.TimeSlotRepository
import com.example.android_2425_gent2.data.repository.user.RemoteUserRepository
import com.example.android_2425_gent2.data.repository.user.TestUserRepository
import com.example.android_2425_gent2.data.repository.user.UserRepository
import com.example.android_2425_gent2.data.repository.battery.BatteryRepository
import com.example.android_2425_gent2.data.repository.battery.TestBatteryRepository
import com.example.android_2425_gent2.data.repository.boat.BoatRepository
import com.example.android_2425_gent2.data.repository.boat.NetworkBoatRepository
import com.example.android_2425_gent2.data.repository.boat.TestBoatRepository
import com.example.android_2425_gent2.di.module.NetworkModule


interface AppContainer {
    val reservationRepository: ReservationRepository
    val timeSlotRepository: TimeSlotRepository
    val notificationRepository: NotificationRepository
    val authRepo: IAuthRepo
    val userRepository: UserRepository
    val batteryRepository: BatteryRepository
    val boatRepository: BoatRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    private val auth0: Auth0 = Auth0.getInstance(context)
    private val authentication: AuthenticationAPIClient = AuthenticationAPIClient(auth0)
    private val database by lazy { AppDatabase.getDatabase(context) }

    private val credentialsManager = SecureCredentialsManager(
        context,
        auth0,
        SharedPreferencesStorage(context)
    )

    override val authRepo: IAuthRepo by lazy {
        Auth0Repo(authentication, credentialsManager,database)
    }

    override val reservationRepository: ReservationRepository by lazy {
        OfflineFirstReservationRepository(AppDatabase.getDatabase(context).offlineReservationDao()
        , NetworkModule.provideReservationApiService(authRepo))
    }

    override val timeSlotRepository: TimeSlotRepository by lazy {
        NetworkTimeSlotRepository(NetworkModule.provideTimeSlotApiService(authRepo))
    }

    override val notificationRepository: NotificationRepository by lazy {
        OfflineFirstNotificationRepository(AppDatabase.getDatabase(context).offlineNotificationDao(),
            NetworkModule.provideNotificationApiService(authRepo))
    }
    override val userRepository: UserRepository by lazy {
        RemoteUserRepository(NetworkModule.provideUserApiSerivce(authRepo))
    }
    override val batteryRepository: BatteryRepository by lazy {
        NetworkBatteryRepository(NetworkModule.provideBatteryApiService(authRepo))
    }
    override val boatRepository: BoatRepository by lazy {
        NetworkBoatRepository(NetworkModule.provideBoatApiService(authRepo))
    }
}

class TestContainer() : AppContainer {

    override val authRepo: IAuthRepo by lazy {
        TestAuth0Repo()
    }

    override val reservationRepository: ReservationRepository by lazy {
        TestReservationRepository()
    }
    override val timeSlotRepository: TimeSlotRepository by lazy {
        TestTimeSlotRepository()
    }
    override val notificationRepository: NotificationRepository by lazy {
        TestNotificationRepository()
    }
    override val userRepository: UserRepository by lazy {
        TestUserRepository()
    }
    override val batteryRepository: BatteryRepository by lazy {
        TestBatteryRepository()
    }
    override val boatRepository: BoatRepository by lazy {
        TestBoatRepository()
    }
}
