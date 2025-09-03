package com.example.android_2425_gent2.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.android_2425_gent2.data.local.entity.UserReservationCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface UserReservationDao : EntityDao<UserReservationCrossRef> {
    @Query("SELECT * from user_reservation")
    fun getAllUserReservations(): Flow<List<UserReservationCrossRef>>
}