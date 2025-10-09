package ru.samtakoy.features.database.data

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.samtakoy.features.card.data.CardDao
import ru.samtakoy.features.card.data.CardEntity
import ru.samtakoy.features.learncourse.data.LearnCourseDao
import ru.samtakoy.features.learncourse.data.model.LearnCourseEntity
import ru.samtakoy.features.learncourseview.data.LearnCourseViewDao
import ru.samtakoy.features.learncourseview.data.LearnCourseViewEntity
import ru.samtakoy.features.qpack.data.QPackDao
import ru.samtakoy.features.qpack.data.QPackEntity
import ru.samtakoy.features.tag.data.CardTagDao
import ru.samtakoy.features.tag.data.CardTagEntity
import ru.samtakoy.features.tag.data.TagDao
import ru.samtakoy.features.tag.data.TagEntity
import ru.samtakoy.features.theme.data.ThemeDao
import ru.samtakoy.features.theme.data.ThemeEntity
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