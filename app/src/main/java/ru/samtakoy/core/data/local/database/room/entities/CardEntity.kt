package ru.samtakoy.core.data.local.database.room.entities

import androidx.room.*
import ru.samtakoy.core.data.local.database.room.converters.DateLongConverter
import ru.samtakoy.core.data.local.database.room.converters.ImageListConverter
import ru.samtakoy.core.data.local.database.room.entities.CardEntity.Companion._qpack_id
import java.util.*
import kotlin.collections.ArrayList

@Entity(tableName = CardEntity.table,
        foreignKeys = [
            ForeignKey(
                    entity = QPackEntity::class,
                    parentColumns = ["_id"],
                    childColumns = [_qpack_id],
                    onDelete = ForeignKey.RESTRICT)
        ])
//@TypeConverters(ImageListConverter::class, DateLongConverter::class)
class CardEntity(

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = CardEntity._id) public var id: Long,
        @ColumnInfo(name = CardEntity._qpack_id, index = true) var qPackId: Long,
        @ColumnInfo(name = CardEntity._question) var question: String,
        @ColumnInfo(name = CardEntity._answer) var answer: String,

        @field:TypeConverters(ImageListConverter::class)
        @ColumnInfo(name = CardEntity._aimages)
        var aImages: List<String>,
        @ColumnInfo(name = CardEntity._comment) var comment: String,
        @ColumnInfo(name = CardEntity._views) var views: Int,
        @ColumnInfo(name = CardEntity._errors) var errors: Int,
        @ColumnInfo(name = CardEntity._last_good_views) var lastGoodViews: Int,
        @ColumnInfo(name = CardEntity._last_errors) var lastErros: Int,

        @ColumnInfo(name = CardEntity._last_view_date)
        @field:TypeConverters(DateLongConverter::class)
        var lastViewDate: Date

) {

    constructor() : this(
            0L, 0L, "", "", emptyList<String>(), "",
            0, 0, 0, 0, Date(0)
    )

    companion object {
        const val table = "cards"
        const val _id = "_id"

        const val _qpack_id = "qpack_id"
        const val _question = "question"
        const val _answer = "answer"
        const val _aimages = "aimages"
        const val _comment = "comment"
        const val _views = "views"
        const val _errors = "errors"
        const val _last_good_views = "last_good_views"
        const val _last_errors = "last_errors"
        const val _last_view_date = "last_view_date"

        fun initNew(qPackId: Long, question: String, answer: String, comment: String): CardEntity {

            return CardEntity(
                    0L, qPackId, question, answer, ArrayList<String>(), comment,
                    0, 0, 0, 0, Date(0)
            )
        }


    }

}