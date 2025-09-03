package com.example.android_2425_gent2.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android_2425_gent2.data.local.AppDatabase
import com.example.android_2425_gent2.data.local.entity.OfflineNotificationEntity
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
class NotificationDaoTest {
    private lateinit var notificationDao: OfflineNotificationDao
    private lateinit var appDatabase: AppDatabase

    private val notification1 = OfflineNotificationEntity(
        id = 1,
        title = "Test Notification 1",
        message = "Test Message 1",
        severity = 1,
        createdAt = LocalDateTime.now(),
        isRead = false
    )

    private val notification2 = OfflineNotificationEntity(
        id = 2,
        title = "Test Notification 2",
        message = "Test Message 2",
        severity = 2,
        createdAt = LocalDateTime.now().minusHours(1),
        isRead = true
    )

    private fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        notificationDao = appDatabase.offlineNotificationDao()
    }

    @Before
    fun setup() {
        createDb()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        appDatabase.close()
    }

    private suspend fun addNotificationsToDb() {
        notificationDao.insert(notification1)
        notificationDao.insert(notification2)
    }


    @Test
    @Throws(Exception::class)
    fun daoGetNotifications_returnsAllNotificationsOfCurrentUserInDb() = runBlocking {
        addNotificationsToDb()
        val notifications = notificationDao.getOfflineNotifications().first()
        assertEquals(
            notifications.count(), 2
        )
    }
}