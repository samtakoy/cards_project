package ru.samtakoy.core.database.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = ThemeEntity.table, indices = [Index(ThemeEntity._parent)])
class ThemeEntity(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = ThemeEntity._id) var id: Long,
        @ColumnInfo(name = ThemeEntity._title) var title: String,
        @ColumnInfo(name = ThemeEntity._parent) var parentId: Long
) {

    companion object {

        const val table = "themes"

        const val _id = "_id"
        const val _title = "title"
        const val _parent = "parent"
    }
}