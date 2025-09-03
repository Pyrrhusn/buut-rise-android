package com.example.android_2425_gent2.data.repository.notification

import android.util.Log
import com.example.android_2425_gent2.data.local.dao.OfflineNotificationDao
import com.example.android_2425_gent2.data.local.entity.asExternalModel
import com.example.android_2425_gent2.data.model.Notification
import com.example.android_2425_gent2.data.network.model.asEntity
import com.example.android_2425_gent2.data.network.notification.NotificationApiService
import com.example.android_2425_gent2.data.repository.APIResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class OfflineFirstNotificationRepository(
    private val notificationDao: OfflineNotificationDao,
    private val remoteApiService: NotificationApiService
): NotificationRepository {

    private val _notifications = MutableStateFlow<APIResource<List<Notification>>>(APIResource.Loading())
    override val notifications: Flow<APIResource<List<Notification>>> = _notifications.asStateFlow()

    private suspend fun fetchAndStoreNotifications() {
        try {
            println("Trying to fetch data")
            val response = remoteApiService.getNotifications()

            withContext(Dispatchers.IO) {
                notificationDao.insert(response.map { it.asEntity() })
            }

            delay(100)
        } catch (e: Exception) {
            println("Inside exception")
            e.printStackTrace()
            Log.e("Inside exception", e.message ?: "unknown message")
            val localData = notificationDao.getOfflineNotifications().first()
            if(localData.isEmpty()) {
                throw e
            }
        }
    }

    override suspend fun getNotifications(): Flow<APIResource<List<Notification>>> = flow {
        emit(APIResource.Loading())
        _notifications.emit(APIResource.Loading())

        val notificationsFlow = notificationDao.getOfflineNotifications()
            .distinctUntilChanged()
            .map { localNotifications ->
                APIResource.Success(
                    localNotifications.map { it.asExternalModel() }
                )
            }

        try {
            fetchAndStoreNotifications()
        } catch (e: Exception) {
            val error = APIResource.Error<List<Notification>>("No notifications found")
            emit(error)
            _notifications.emit(error)
            return@flow
        }

        notificationsFlow.collect { emission ->
            emit(emission)
            _notifications.emit(emission)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun markNotificationAsRead(id: Int): Flow<APIResource<Unit>> = flow {
        emit(APIResource.Loading())

        try {
            // Call the remote API to mark as read
            remoteApiService.markNotificationAsRead(id)

            // If successful, update the local database and refresh all notifications
            withContext(Dispatchers.IO) {
                notificationDao.getNotificationById(id).first()?.let { notification ->
                    notificationDao.insert(notification.copy(isRead = true))
                }
            }

            // Fetch fresh notifications after marking as read
            fetchAndStoreNotifications()

            // Update shared flow with latest notifications
            val latestNotifications = notificationDao.getOfflineNotifications().first()
            _notifications.emit(APIResource.Success(latestNotifications.map { it.asExternalModel() }))

            emit(APIResource.Success(Unit))
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("MarkAsRead", e.message ?: "unknown message")
            emit(APIResource.Error("Failed to mark notification as read"))
        }
    }.flowOn(Dispatchers.IO)
}