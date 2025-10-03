package ru.samtakoy.core.data.local.database.room.entities

import androidx.room.*
import ru.samtakoy.core.app.utils.DateUtils
import ru.samtakoy.core.data.local.database.room.converters.DateLongConverter
import java.text.ParseException
import java.text.SimpleDateFormat

const val DEF_DATE = "01-01-2000 00:00:00"
private val DATE_FORMAT = ru.samtakoy.core.utils.DateUtils.DATE_FORMAT

@Entity(tableName = QPackEntity.table,
        foreignKeys = [
            ForeignKey(
                entity = ThemeEntity::class,
                parentColumns = [QPackEntity._id],
                childColumns = [QPackEntity._theme_id],
                onDelete = ForeignKey.RESTRICT)
        ])
class QPackEntity(

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
    val creationDate: java.util.Date,
    @ColumnInfo(name = _view_counter)
    val viewCount: Int,
    @field:TypeConverters(DateLongConverter::class)
    @ColumnInfo(name = _last_view_date)
    val lastViewDate: java.util.Date,
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


    fun getCreationDateAsString(): String {
        return DATE_FORMAT.format(creationDate)
    }

    fun getLastViewDateAsString(): String {
        return DATE_FORMAT.format(lastViewDate)
    }

    fun getExportFileName(): String {
        val result: String = if (fileName.length > 0) fileName else id.toString()
        return if (result.indexOf('.') < 0) "$result.txt" else result
    }

    fun hasTitle(): Boolean {
        return title.isNotEmpty()
    }

    fun hasDesc(): Boolean {
        return desc.isNotEmpty()
    }

}