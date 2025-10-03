package ru.samtakoy.core.data.local.database.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import ru.samtakoy.core.data.local.database.DB_VERSION
import ru.samtakoy.core.data.local.database.room.dao.*
import ru.samtakoy.core.data.local.database.room.entities.*
import ru.samtakoy.features.views.data.local.model.ViewHistoryDao
import ru.samtakoy.features.views.data.local.model.ViewHistoryEntity

@Database(
    version = DB_VERSION,
    entities = [
        CardEntity::class,
        CardTagEntity::class,
        LearnCourseEntity::class,
        QPackEntity::class,
        TagEntity::class,
        ThemeEntity::class,
        ThemeTagEntity::class,
        ViewHistoryEntity::class,
        LearnCourseViewEntity::class
    ]/*,
    autoMigrations = [
        AutoMigration(from = 5, to = 6),
        AutoMigration(from = 6, to = 7),
        AutoMigration(from = 7, to = 8)
    ]*/
)

abstract class MyRoomDb() : RoomDatabase() {
    abstract fun cardDao(): CardDao
    abstract fun themeDao(): ThemeDao
    abstract fun qPackDao(): QPackDao
    abstract fun courseDao(): LearnCourseDao
    abstract fun tagDao(): TagDao
    abstract fun cardTagDao(): CardTagDao
    abstract fun viewHistoryDao(): ViewHistoryDao
    abstract fun courseViewDao(): LearnCourseViewDao
}