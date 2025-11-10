package ru.samtakoy.data.common.di

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import org.koin.dsl.module
import ru.samtakoy.data.common.db.DB_NAME
import ru.samtakoy.data.common.db.MyRoomDb

internal actual fun commonPlatformDataModule() = module {
    single<MyRoomDb> {
        val context = get<Context>().applicationContext
        val dbFile = context.getDatabasePath(DB_NAME)
        Room.databaseBuilder<MyRoomDb>(context, dbFile.absolutePath)
            .setDriver(BundledSQLiteDriver())
            // .addMigrations(MIGRATION_5_6)
            .fallbackToDestructiveMigration(true)
        .build()
    }
}