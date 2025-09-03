package com.example.android_2425_gent2.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.android_2425_gent2.data.local.entity.OfflineReservationEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface OfflineReservationDao : EntityDao<OfflineReservationEntity> {

    @Transaction
    @Query(
        """
    SELECT * FROM offline_reservation 
    WHERE CASE 
        WHEN :getPast = 1 THEN date < :currentDate
        ELSE date >= :currentDate
    END
    ORDER BY date ASC, start ASC
"""
    )
    fun getOfflineReservations(
        getPast: Boolean,
        currentDate: LocalDate = LocalDate.now()
    ): Flow<List<OfflineReservationEntity>>

    @Transaction
    @Query(
        """
        SELECT * FROM offline_reservation 
        ORDER BY date DESC, start DESC
        LIMIT :pageSize OFFSET :offset
    """
    )
    fun getOfflineReservationsWithOffset(
        offset: Int = 0,
        pageSize: Int = 10
    ): Flow<List<OfflineReservationEntity>>

    @Query("SELECT COUNT(*) FROM offline_reservation")
    suspend fun getCount(): Int

    @Query("SELECT * FROM offline_reservation WHERE id = :id")
    suspend fun getOfflineReservationByIdForCancel(id: Int): OfflineReservationEntity?
   // Add to existing interface
   @Query("SELECT * FROM offline_reservation WHERE id = :reservationId")
    fun getOfflineReservationById(reservationId: Int): Flow<OfflineReservationEntity?>


}