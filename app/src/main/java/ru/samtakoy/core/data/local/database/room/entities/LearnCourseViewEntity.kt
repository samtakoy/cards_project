package ru.samtakoy.core.data.local.database.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.samtakoy.core.data.local.database.room.entities.LearnCourseViewEntity.Companion.COURSE_ID
import ru.samtakoy.core.data.local.database.room.entities.LearnCourseViewEntity.Companion.VIEW_ID
import ru.samtakoy.features.views.data.local.model.ViewHistoryEntity

@Entity(
    tableName = LearnCourseViewEntity.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = LearnCourseEntity::class,
            parentColumns = arrayOf(LearnCourseEntity._id),
            childColumns = arrayOf(COURSE_ID),
            onDelete = CASCADE,
            onUpdate = CASCADE,
            deferred = true
        ),
        ForeignKey(
            entity = ViewHistoryEntity ::class,
            parentColumns = arrayOf(ViewHistoryEntity.ID),
            childColumns = arrayOf(VIEW_ID),
            onDelete = CASCADE,
            onUpdate = CASCADE,
            deferred = true
        )
    ],
    indices = [Index(value = [COURSE_ID])]
)
class LearnCourseViewEntity(
    @PrimaryKey
    @ColumnInfo(name = VIEW_ID)
    val viewId: Long,
    @ColumnInfo(name = COURSE_ID)
    val courseId: Long
) {
    companion object {
        const val TABLE_NAME = "course_view_relation"
        const val COURSE_ID = "course_id"
        const val VIEW_ID = "view_id"
    }
}