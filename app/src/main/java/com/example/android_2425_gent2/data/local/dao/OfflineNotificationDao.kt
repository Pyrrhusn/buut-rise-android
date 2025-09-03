package com.example.android_2425_gent2.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.android_2425_gent2.data.local.entity.OfflineNotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OfflineNotificationDao: EntityDao<OfflineNotificationEntity> {

    @Transaction
    @Query(
        """
            SELECT * FROM offline_notification
        """
    )
    fun getOfflineNotifications(): Flow<List<OfflineNotificationEntity>>

    @Transaction
    @Query("SELECT * FROM offline_notification WHERE id = :id")
    fun getNotificationById(id: Int): Flow<OfflineNotificationEntity?>

}