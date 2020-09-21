package ru.samtakoy.core.data.local.database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.samtakoy.core.data.local.database.DB_VERSION
import ru.samtakoy.core.data.local.database.room.dao.*
import ru.samtakoy.core.data.local.database.room.entities.*

@Database(entities = [
    CardEntity::class,
    CardTagEntity::class,
    LearnCourseEntity::class,
    QPackEntity::class,
    TagEntity::class,
    ThemeEntity::class,
    ThemeTagEntity::class
], version = DB_VERSION)

abstract class MyRoomDb() : RoomDatabase() {

    abstract fun cardDao(): CardDao
    abstract fun themeDao(): ThemeDao
    abstract fun qPackDao(): QPackDao
    abstract fun courseDao(): LearnCourseDao
    abstract fun tagDao(): TagDao
    abstract fun cardTagDao(): CardTagDao


}