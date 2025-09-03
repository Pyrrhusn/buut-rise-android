package com.example.android_2425_gent2.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.android_2425_gent2.data.local.dao.BoatDao
import com.example.android_2425_gent2.data.local.dao.OfflineNotificationDao
import com.example.android_2425_gent2.data.local.dao.OfflineReservationDao
import com.example.android_2425_gent2.data.local.dao.ReservationDao
import com.example.android_2425_gent2.data.local.dao.TimeSlotDao
import com.example.android_2425_gent2.data.local.dao.UserDao
import com.example.android_2425_gent2.data.local.dao.UserReservationDao
import com.example.android_2425_gent2.data.local.entity.BoatEntity
import com.example.android_2425_gent2.data.local.entity.OfflineNotificationEntity
import com.example.android_2425_gent2.data.local.entity.OfflineReservationEntity
import com.example.android_2425_gent2.data.local.entity.ReservationEntity
import com.example.android_2425_gent2.data.local.entity.TimeSlotEntity
import com.example.android_2425_gent2.data.local.entity.UserEntity
import com.example.android_2425_gent2.data.local.entity.UserReservationCrossRef
import com.example.android_2425_gent2.data.local.migration.MIGRATION_1_2

@Database(
    entities = [
        UserEntity::class,
        BoatEntity::class,
        ReservationEntity::class,
        TimeSlotEntity::class,
        UserReservationCrossRef::class,
        OfflineReservationEntity::class,
        OfflineNotificationEntity::class
    ],
    exportSchema = false,
    version = 2
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_database",
                ).addMigrations(MIGRATION_1_2)
                    .build().also { Instance = it }
            }

        }
    }

    abstract fun userDao(): UserDao
    abstract fun reservationDao(): ReservationDao
    abstract fun userReservationDao(): UserReservationDao
    abstract fun timeSlotDao(): TimeSlotDao
    abstract fun boatDao(): BoatDao
    abstract fun offlineReservationDao(): OfflineReservationDao
    abstract fun offlineNotificationDao(): OfflineNotificationDao
}
