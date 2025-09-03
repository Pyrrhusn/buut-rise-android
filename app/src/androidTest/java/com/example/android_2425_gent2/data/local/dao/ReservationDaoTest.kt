package com.example.android_2425_gent2.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android_2425_gent2.data.local.AppDatabase
import com.example.android_2425_gent2.data.local.entity.BoatEntity
import com.example.android_2425_gent2.data.local.entity.ReservationEntity
import com.example.android_2425_gent2.data.local.entity.TimeSlotEntity
import com.example.android_2425_gent2.data.local.entity.UserEntity
import com.example.android_2425_gent2.data.local.entity.UserReservationCrossRef
import com.example.android_2425_gent2.data.local.entity.linking_entities.ReservationWithTimeSlotAndBoatEntity
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.LocalDate
import java.time.LocalTime

@RunWith(AndroidJUnit4::class)
class ReservationDaoTest {
    private lateinit var reservationDao: ReservationDao
    private lateinit var userDao: UserDao
    private lateinit var userReservationDao: UserReservationDao
    private lateinit var boatDao: BoatDao
    private lateinit var timeSlotDao: TimeSlotDao

    private lateinit var appDatabase: AppDatabase
    private val reservation1 = ReservationEntity(
        reservationId = 1,
        boatId = null,
        timeSlotId = null,
        batteryId = null
    )
    private val reservation2 = ReservationEntity(
        reservationId = 2,
        boatId = null,
        timeSlotId = null,
        batteryId = null
    )
    private val boat1 = BoatEntity(
        boatId = 1,
        name = "boat 1"
    )
    private val user1 = UserEntity(userId = 1)


    private fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        reservationDao = appDatabase.reservationDao()
        userDao = appDatabase.userDao()
        userReservationDao = appDatabase.userReservationDao()
        boatDao = appDatabase.boatDao()
        timeSlotDao = appDatabase.timeSlotDao()
    }

    @Before
    fun setup() {
        createDb()
        runBlocking {
            insertUser()
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        appDatabase.close()
    }

    private suspend fun insertUser() {
        userDao.insert(user1)
    }

    private suspend fun addOneReservationToDb() {
        reservationDao.insert(reservation1)
    }

    private suspend fun addTwoReservationsToDb() {
        reservationDao.insert(reservation1)
        reservationDao.insert(reservation2)
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsReservationIntoDB() = runBlocking {
        addOneReservationToDb()
        val reservation: ReservationEntity? = reservationDao.getReservationById(1).first()
        assertEquals(reservation, reservation1)
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdate_updatesReservationInDB() = runBlocking {
        val updatedReservation = ReservationEntity(
            reservationId = 1, boatId = 3, timeSlotId = 3,
            batteryId = 1
        )
        addOneReservationToDb()
        reservationDao.update(updatedReservation)
        val reservation: ReservationEntity? = reservationDao.getReservationById(1).first()
        assertEquals(reservation, updatedReservation)
    }

    @Test
    @Throws(Exception::class)
    fun daoDelete_deletesReservationInDB() = runBlocking {
        addOneReservationToDb()
        reservationDao.delete(reservation1)
        val reservation: ReservationEntity? = reservationDao.getReservationById(1).first()
        assertEquals(reservation, null)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetReservationsByUser_returnsAllReservationsOfUserInDB() = runBlocking {
        addTwoReservationsToDb()
        val userReservationCrossRef1: UserReservationCrossRef =
            UserReservationCrossRef(user1.userId, reservation1.reservationId)
        val userReservationCrossRef2: UserReservationCrossRef =
            UserReservationCrossRef(user1.userId, reservation2.reservationId)
        userReservationDao.insert(userReservationCrossRef1)
        userReservationDao.insert(userReservationCrossRef2)

        val reservations = reservationDao.getReservationsByUser(user1.userId).first()
        assertEquals(
            reservations[0],
            ReservationWithTimeSlotAndBoatEntity(reservation1, null, null)
        )
        assertEquals(
            reservations[1],
            ReservationWithTimeSlotAndBoatEntity(reservation2, null, null)
        )
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllReservationsAfterDateAndTime_returnsAllReservationsAfterGivenDateAndTime() =
        runBlocking {
            val date = LocalDate.of(2024, 10, 1)
            val time = LocalTime.of(0, 0)
            val days: MutableList<List<TimeSlotEntity>> = mutableListOf()
            val reservations: MutableList<ReservationEntity> = mutableListOf()

            seedReservationsWithTimeSlots(days, reservations, date, time)

            val receivedReservations = reservationDao.getAllReservationsAfterDateAndTime(
                date.plusDays(2),
                time.plusHours(6)
            ).first()
            val expectedTimeSlots = listOf(days[1][1], days[1][2]) + days[2]
            val expectedReservations = reservations.map { r ->
                ReservationWithTimeSlotAndBoatEntity(
                    reservation = r,
                    timeSlot = expectedTimeSlots.find {
                        it.timeSlotId == r.timeSlotId
                    },
                    boat = null
                )
            }.filter { it.timeSlot != null }.toList()

            assert(expectedReservations == receivedReservations)
        }

    @Test
    @Throws(Exception::class)
    fun daoGetAllReservationsBeforeDateAndTime_returnsAllReservationsBeforeGivenDateAndTime() =
        runBlocking {
            val date = LocalDate.of(2024, 10, 1)
            val time = LocalTime.of(0, 0)
            val days: MutableList<List<TimeSlotEntity>> = mutableListOf()
            val reservations: MutableList<ReservationEntity> = mutableListOf()

            seedReservationsWithTimeSlots(days, reservations, date, time)

            val receivedReservations = reservationDao.getAllReservationsBeforeDateAndTime(
                date.plusDays(2),
                time.plusHours(6)
            ).first()
            val expectedTimeSlots = listOf(days[1][0]) + days[0]
            val expectedReservations = reservations.map { r ->
                ReservationWithTimeSlotAndBoatEntity(
                    reservation = r,
                    timeSlot = expectedTimeSlots.find {
                        it.timeSlotId == r.timeSlotId
                    },
                    boat = null
                )
            }.filter { it.timeSlot != null }.toList()

            assert(expectedReservations == receivedReservations)
        }

    private suspend fun seedReservationsWithTimeSlots(
        days: MutableList<List<TimeSlotEntity>>,
        reservations: MutableList<ReservationEntity>,
        date: LocalDate,
        time: LocalTime,
    ) {
        (1..3).forEach { day ->
            val timeSlotsPerDay = 3
            days.add((1..timeSlotsPerDay).map {
                val timeSlot = TimeSlotEntity(
                    timeSlotId = it + (day - 1) * timeSlotsPerDay,
                    date = date.plusDays(day.toLong()),
                    start = time.plusHours((it * 3).toLong()),
                    end = time.plusHours((it * 3 + 3).toLong())
                )
                reservations.add(
                    ReservationEntity(
                        reservationId = timeSlot.timeSlotId,
                        boatId = null,
                        batteryId = null,
                        timeSlotId = timeSlot.timeSlotId
                    )
                )
                return@map timeSlot
            }.toList())
        }

        reservationDao.insert(reservations)
        for (timeSlotEntities in days) {
            timeSlotDao.insert(timeSlotEntities)
        }
    }
}