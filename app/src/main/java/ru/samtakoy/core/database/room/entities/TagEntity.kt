package ru.samtakoy.core.database.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey


@Entity(tableName = TagEntity.table)
class TagEntity(

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = TagEntity._id) var id: Long,
        title: String

) {


    companion object {

        const val table = "tags"
        const val _id = "_id"
        const val _title = "title"

        fun titleToKey(title: String): String {
            return title.toLowerCase()
        }

        fun initNew(title: String): TagEntity {
            return TagEntity(0L, title)
        }
    }

    @ColumnInfo(name = TagEntity._title)
    var title: String = title
        set(value) {
            field = value
            key = titleToKey(value)
        }
        get() = field

    @Ignore
    var key: String = ""
        private set


    fun hasId(): Boolean = id > 0
}