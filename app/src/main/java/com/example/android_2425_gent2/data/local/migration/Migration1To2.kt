package com.example.android_2425_gent2.data.local.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS offline_notification (
                id INTEGER PRIMARY KEY NOT NULL,
                severity INTEGER NOT NULL,
                title TEXT NOT NULL,
                message TEXT NOT NULL,
                createdAt TEXT NOT NULL,
                isRead INTEGER NOT NULL
            )
            """.trimIndent()
        )
    }
}