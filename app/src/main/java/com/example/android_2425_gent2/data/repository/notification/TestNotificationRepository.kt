package com.example.android_2425_gent2.data.repository.notification


import com.example.android_2425_gent2.data.model.Notification
import com.example.android_2425_gent2.data.repository.APIResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.Date

class TestNotificationRepository: NotificationRepository {
    private val _notifications = MutableStateFlow<APIResource<List<Notification>>>(APIResource.Loading())
    override val notifications: Flow<APIResource<List<Notification>>> = _notifications.asStateFlow()

    private val mockNotifications = mutableListOf(
        Notification(
            id = 1,
            severity = 1,
            title = "This is a info notification",
            message = "This is the message",
            timeStamp = Date(),
            isRead = false
        ),
        Notification(
            id = 2,
            severity = 1,
            title = "This is a info notification",
            message = "This is the message",
            timeStamp = Date(),
            isRead = false
        )
    )
  
    
    override suspend fun getNotifications(): Flow<APIResource<List<Notification>>> = flow {
        emit(APIResource.Loading())
        delay(100)
        val result = APIResource.Success(mockNotifications.toList())
        _notifications.emit(result)
        emit(result)
    }.flowOn(Dispatchers.IO)

    override suspend fun markNotificationAsRead(id: Int): Flow<APIResource<Unit>> = flow {
        emit(APIResource.Loading())
        val index = mockNotifications.indexOfFirst { it.id == id }
        if (index != -1) {
            mockNotifications[index] = mockNotifications[index].copy(isRead = true)
            _notifications.emit(APIResource.Success(mockNotifications.toList()))
            emit(APIResource.Success(Unit))
        } else {
            emit(APIResource.Error("Notification not found"))
        }
    }.flowOn(Dispatchers.IO)
}