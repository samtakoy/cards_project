package ru.samtakoy.core.data.local.database.room.entities

import androidx.room.*
import ru.samtakoy.core.data.local.database.room.converters.DateLongConverter
import ru.samtakoy.core.data.local.database.room.converters.ImageListConverter
import ru.samtakoy.core.data.local.database.room.entities.CardEntity.Companion._qpack_id
import java.util.*

@Entity(tableName = CardEntity.table,
        foreignKeys = [
            ForeignKey(
                    entity = QPackEntity::class,
                    parentColumns = ["_id"],
                    childColumns = [_qpack_id],
                    onDelete = ForeignKey.RESTRICT)
        ])
//@TypeConverters(ImageListConverter::class, DateLongConverter::class)
data class CardEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = _id)
    val id: Long,
    @ColumnInfo(name = _qpack_id, index = true)
    val qPackId: Long,
    @ColumnInfo(name = _question)
    val question: String,
    @ColumnInfo(name = _answer)
    val answer: String,

    @field:TypeConverters(ImageListConverter::class)
    @ColumnInfo(name = _aimages)
    val aImages: List<String>,
    @ColumnInfo(name = _comment)
    val comment: String,
    @ColumnInfo(name = _views)
    val views: Int,
    @ColumnInfo(name = _errors)
    val errors: Int,
    @ColumnInfo(name = _last_good_views)
    val lastGoodViews: Int,
    @ColumnInfo(name = _last_errors)
    val lastErros: Int,
    @ColumnInfo(name = _last_view_date)
    @field:TypeConverters(DateLongConverter::class)
    val lastViewDate: Date,
    @ColumnInfo(name = _favorite, defaultValue = "0")
    val favorite: Int

) {

    constructor() : this(
        id = 0L,
        qPackId = 0L,
        question = "",
        answer = "",
        aImages = emptyList<String>(),
        comment = "",
        views = 0,
        errors = 0,
        lastGoodViews = 0,
        lastErros = 0,
        lastViewDate = Date(0),
        favorite = 0
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
        const val _favorite = "favorite"

        fun initNew(
            id: Long,
            qPackId: Long,
            question: String,
            answer: String,
            aImages: List<String>,
            comment: String,
            favorite: Int = 0
        ): CardEntity {

            return CardEntity(
                id = id,
                qPackId = qPackId,
                question = question,
                answer = answer,
                aImages = aImages,
                comment = comment,
                views = 0,
                errors = 0,
                lastGoodViews = 0,
                lastErros = 0,
                lastViewDate = Date(0),
                favorite = favorite
            )
        }


    }

}