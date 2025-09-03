package com.example.android_2425_gent2.data.repository.notification

import com.example.android_2425_gent2.data.model.Notification
import com.example.android_2425_gent2.data.repository.APIResource
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    val notifications: Flow<APIResource<List<Notification>>>
    suspend fun getNotifications(): Flow<APIResource<List<Notification>>>
    suspend fun markNotificationAsRead(id: Int): Flow<APIResource<Unit>>
}