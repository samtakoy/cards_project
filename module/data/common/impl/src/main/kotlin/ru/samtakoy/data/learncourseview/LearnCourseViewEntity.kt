package ru.samtakoy.data.learncourseview

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.samtakoy.data.learncourse.model.LearnCourseEntity
import ru.samtakoy.data.view.model.ViewHistoryEntity

@Entity(
    tableName = LearnCourseViewEntity.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = LearnCourseEntity::class,
            parentColumns = arrayOf(LearnCourseEntity.Companion._id),
            childColumns = arrayOf(LearnCourseViewEntity.Companion.COURSE_ID),
            onDelete = CASCADE,
            onUpdate = CASCADE,
            deferred = true
        ),
        ForeignKey(
            entity = ViewHistoryEntity ::class,
            parentColumns = arrayOf(ViewHistoryEntity.ID),
            childColumns = arrayOf(LearnCourseViewEntity.Companion.VIEW_ID),
            onDelete = CASCADE,
            onUpdate = CASCADE,
            deferred = true
        )
    ],
    indices = [Index(value = [LearnCourseViewEntity.Companion.COURSE_ID])]
)
internal class LearnCourseViewEntity(
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