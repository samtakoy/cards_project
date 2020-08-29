package ru.samtakoy.core.database.room.entities

import androidx.room.*
import ru.samtakoy.core.database.room.converters.DateLongConverter
import ru.samtakoy.core.utils.DateUtils
import java.text.ParseException
import java.text.SimpleDateFormat

private const val DATE_PATTERN = "dd-MM-yyyy HH:mm:ss"
const val DEF_DATE = "01-01-2000 00:00:00"
private val DATE_FORMAT = SimpleDateFormat(DATE_PATTERN)

@Entity(tableName = QPackEntity.table,
        foreignKeys = [
            ForeignKey(
                    entity = ThemeEntity::class,
                    parentColumns = ["_id"],
                    childColumns = ["theme_id"],
                    onDelete = ForeignKey.RESTRICT)
        ])
class QPackEntity(

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = QPackEntity._id) var id: Long,
        @ColumnInfo(name = QPackEntity._theme_id, index = true) var themeId: Long,
        @ColumnInfo(name = QPackEntity._path) var path: String,
        @ColumnInfo(name = QPackEntity._file_name) var fileName: String,
        @ColumnInfo(name = QPackEntity._title) var title: String,
        @ColumnInfo(name = QPackEntity._desc) var desc: String,
        @field:TypeConverters(DateLongConverter::class)
        @ColumnInfo(name = QPackEntity._creation_date) var creationDate: java.util.Date,
        @ColumnInfo(name = QPackEntity._view_counter) var viewCount: Int,
        @field:TypeConverters(DateLongConverter::class)
        @ColumnInfo(name = QPackEntity._last_view_date) var lastViewDate: java.util.Date
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


        fun initNew(themeId: Long, path: String, fileName: String, title: String, desc: String): QPackEntity {
            val creationDate: java.util.Date = java.util.Date(DateUtils.getCurrentTimeLong())
            return QPackEntity(0L, themeId, path, fileName, title, desc, creationDate, 0, creationDate)
        }
    }


    fun parseCreationDateFromString(src: String): Boolean {
        try {
            this.creationDate = DATE_FORMAT.parse(src)!!
        } catch (e: ParseException) {
            return false
        }
        return true
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