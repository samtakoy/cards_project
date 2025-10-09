package ru.samtakoy.features.tag.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey


@Entity(tableName = TagEntity.table)
class TagEntity(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = _id)
        val id: Long,
        @ColumnInfo(name = _title)
        val title: String
) {
    companion object {
        const val table = "tags"
        const val _id = "_id"
        const val _title = "title"
    }
}