package com.example.android_2425_gent2.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android_2425_gent2.data.local.AppDatabase
import com.example.android_2425_gent2.data.local.entity.ReservationEntity
import com.example.android_2425_gent2.data.local.entity.UserEntity
import com.example.android_2425_gent2.data.local.entity.UserReservationCrossRef
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class UserReservationDaoTest {
    private lateinit var appDatabase: AppDatabase
    private lateinit var userReservationDao: UserReservationDao
    private lateinit var reservationDao: ReservationDao
    private lateinit var userDao: UserDao
    private val user1 = UserEntity(1)
    private val reservation1 = ReservationEntity(
        reservationId = 1,
        boatId = 1,
        timeSlotId = 1,
        batteryId = 1
    )
    private val userReservation1 = UserReservationCrossRef(userId = 1, reservationId = 1)

    @Before
    fun setup() {
        createDb()
    }

    private fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        userReservationDao = appDatabase.userReservationDao()
        reservationDao = appDatabase.reservationDao()
        userDao = appDatabase.userDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        appDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsUserReservationIntoDB() = runBlocking {
        userDao.insert(user1)
        reservationDao.insert(reservation1)
        userReservationDao.insert(userReservation1)
        val userReservations = userReservationDao.getAllUserReservations().first()
        assertEquals(userReservations[0], userReservation1)
    }
}