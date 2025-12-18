package ru.samtakoy.data.card.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.samtakoy.data.common.db.converters.ImageListConverter
import ru.samtakoy.data.qpack.QPackEntity

@Entity(tableName = CardEntity.table,
        foreignKeys = [
            ForeignKey(
                entity = QPackEntity::class,
                parentColumns = ["_id"],
                childColumns = [CardEntity.Companion._qpack_id],
                onDelete = ForeignKey.RESTRICT)
        ])
internal data class CardEntity(

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
    @ColumnInfo(name = _favorite, defaultValue = "0")
    val favorite: Int
) {

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
        const val _favorite = "favorite"
    }
}