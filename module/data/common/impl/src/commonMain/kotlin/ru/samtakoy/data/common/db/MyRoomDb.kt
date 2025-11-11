package ru.samtakoy.data.common.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.samtakoy.data.card.CardDao
import ru.samtakoy.data.card.model.CardEntity
import ru.samtakoy.data.learncourse.LearnCourseDao
import ru.samtakoy.data.learncourse.model.LearnCourseEntity
import ru.samtakoy.data.learncourseview.LearnCourseViewDao
import ru.samtakoy.data.learncourseview.LearnCourseViewEntity
import ru.samtakoy.data.qpack.QPackDao
import ru.samtakoy.data.qpack.QPackEntity
import ru.samtakoy.data.cardtag.CardTagDao
import ru.samtakoy.data.cardtag.model.CardTagEntity
import ru.samtakoy.data.cardtag.TagDao
import ru.samtakoy.data.cardtag.model.TagEntity
import ru.samtakoy.data.theme.ThemeDao
import ru.samtakoy.data.theme.ThemeEntity
import ru.samtakoy.data.view.model.ViewHistoryDao
import ru.samtakoy.data.view.model.ViewHistoryEntity

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
    ],
    exportSchema = true
    /*,
    autoMigrations = [
        AutoMigration(from = 5, to = 6),
        AutoMigration(from = 6, to = 7),
        AutoMigration(from = 7, to = 8)
    ]*/
)

internal abstract class MyRoomDb() : RoomDatabase() {
    abstract fun cardDao(): CardDao
    abstract fun themeDao(): ThemeDao
    abstract fun qPackDao(): QPackDao
    abstract fun courseDao(): LearnCourseDao
    abstract fun tagDao(): TagDao
    abstract fun cardTagDao(): CardTagDao
    abstract fun viewHistoryDao(): ViewHistoryDao
    abstract fun courseViewDao(): LearnCourseViewDao
}