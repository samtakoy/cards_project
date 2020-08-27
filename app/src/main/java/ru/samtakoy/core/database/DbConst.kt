package ru.samtakoy.core.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


const val DB_VERSION = 3
const val DB_NAME = "cards.db"


val MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // do nothing - to room migration
    }
}




