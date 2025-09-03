package com.example.android_2425_gent2.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.android_2425_gent2.data.local.entity.ReservationEntity
import com.example.android_2425_gent2.data.local.entity.linking_entities.ReservationWithTimeSlotAndBoatEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalTime

@Dao
interface ReservationDao : EntityDao<ReservationEntity> {
    @Transaction
    @Query(
        """
        SELECT r.* 
        FROM reservation r 
        JOIN user_reservation u_r ON r.reservationId = u_r.reservationId
        WHERE u_r.userId = :userId
        """
    )
    fun getReservationsByUser(userId: Int): Flow<List<ReservationWithTimeSlotAndBoatEntity>>

    @Transaction
    @Query(
        """
        SELECT r.* FROM reservation r
        INNER JOIN time_slot ts ON ts.timeSlotId = r.timeSlotId
        ORDER BY ts.date AND ts.start
    """
    )
    fun getAllReservations(): Flow<List<ReservationWithTimeSlotAndBoatEntity>>

    @Transaction
    @Query(
        """
        SELECT r.* FROM reservation r
        INNER JOIN time_slot ts ON ts.timeSlotId = r.timeSlotId
        WHERE (ts.date = :date AND ts.start >= :time) OR ts.date > :date
        ORDER BY ts.date AND ts.start
    """
    )
    fun getAllReservationsAfterDateAndTime(
        date: LocalDate,
        time: LocalTime,
    ): Flow<List<ReservationWithTimeSlotAndBoatEntity>>

    @Transaction
    @Query(
        """
        SELECT r.* FROM reservation r
        INNER JOIN time_slot ts ON ts.timeSlotId = r.timeSlotId
        WHERE (ts.date = :date AND ts.start < :time) OR ts.date < :date
        ORDER BY ts.date AND ts.start
    """
    )
    fun getAllReservationsBeforeDateAndTime(
        date: LocalDate,
        time: LocalTime,
    ): Flow<List<ReservationWithTimeSlotAndBoatEntity>>

    @Query("SELECT * FROM reservation r WHERE r.reservationId = :id")
    fun getReservationById(id: Int): Flow<ReservationEntity?>


}