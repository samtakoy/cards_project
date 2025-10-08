package ru.samtakoy.features.theme.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = ThemeEntity.table, indices = [Index(ThemeEntity._parent)])
class ThemeEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = _id)
    val id: Long,
    @ColumnInfo(name = _title)
    val title: String,
    @ColumnInfo(name = _parent)
    val parentId: Long
) {

    companion object {

        const val table = "themes"

        const val _id = "_id"
        const val _title = "title"
        const val _parent = "parent"
    }
}