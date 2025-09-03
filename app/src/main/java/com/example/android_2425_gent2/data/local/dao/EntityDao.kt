package com.example.android_2425_gent2.data.local.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface EntityDao<T> {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: T)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entities: List<T>)
    
    @Delete
    suspend fun delete(entity: T)
    
    @Update
    suspend fun update(entity: T)
}