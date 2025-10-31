package ru.samtakoy.data.qpack

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.samtakoy.data.common.db.converters.DateLongConverter
import ru.samtakoy.data.theme.ThemeEntity
import java.util.Date

@Entity(tableName = QPackEntity.table,
        foreignKeys = [
            ForeignKey(
                entity = ThemeEntity::class,
                parentColumns = [QPackEntity._id],
                childColumns = [QPackEntity._theme_id],
                onDelete = ForeignKey.RESTRICT)
        ])
internal class QPackEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = _id)
    val id: Long,
    @ColumnInfo(name = _theme_id, index = true)
    val themeId: Long,
    @ColumnInfo(name = _path)
    val path: String,
    @ColumnInfo(name = _file_name)
    val fileName: String,
    @ColumnInfo(name = _title)
    val title: String,
    @ColumnInfo(name = _desc)
    val desc: String,
    @field:TypeConverters(DateLongConverter::class)
    @ColumnInfo(name = _creation_date)
    val creationDate: Date,
    @ColumnInfo(name = _view_counter)
    val viewCount: Int,
    @field:TypeConverters(DateLongConverter::class)
    @ColumnInfo(name = _last_view_date)
    val lastViewDate: Date,
    @ColumnInfo(name = _favorite, defaultValue = "0")
    val favorite: Int
) {

    companion object {
        const val table = "qpacks"

        const val _id = "_id"
        const val _theme_id = "theme_id"
        const val _path = "path"
        const val _file_name = "file_name"
        const val _title = "title"
        const val _desc = "desc"
        const val _creation_date = "creation_date"
        const val _view_counter = "view_counter"
        const val _last_view_date = "last_view_date"
        const val _favorite = "favorite"

    }
}