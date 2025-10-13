package ru.samtakoy.data.view.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.samtakoy.data.common.db.converters.ListOfLongConverter
import ru.samtakoy.data.common.db.converters.DateLongConverter
import ru.samtakoy.data.qpack.QPackEntity
import java.util.Date

@Entity(
    tableName = ViewHistoryEntity.TABLE,
    foreignKeys = [
        ForeignKey(
            entity = QPackEntity::class,
            parentColumns = [QPackEntity._id],
            childColumns = [ViewHistoryEntity.ID],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
internal class ViewHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    val id: Long,

    @ColumnInfo(name = QPACK_ID, index = true)
    val qPackId: Long,

    /** Просмотренные карточки */
    @field:TypeConverters(ListOfLongConverter::class)
    @ColumnInfo(name = VIEWED_CARD_IDS)
    val viewedCardIds: List<Long>,

    /** Еще не просмотренные карточки */
    @field:TypeConverters(ListOfLongConverter::class)
    @ColumnInfo(name = TODO_CARD_IDS)
    val todoCardIds: List<Long>,

    /** Карточки с ошибкой */
    @field:TypeConverters(ListOfLongConverter::class)
    @ColumnInfo(name = ERROR_CARD_IDS)
    val errorCardIds: List<Long>,

    /** Добавленные в избранные во время просмотра */
    @field:TypeConverters(ListOfLongConverter::class)
    @ColumnInfo(name = NEW_FAVS_CARD_IDS)
    val addedToFavsCardIds: List<Long>,

    /** Сколько осталось не просмотренных */
    @ColumnInfo(name = REST_CARD_COUNT)
    val restCardCount: Int,

    /** Дата поледнего просмотра */
    @field:TypeConverters(DateLongConverter::class)
    @ColumnInfo(name = LAST_VIEW_DATE)
    var lastViewDate: Date
) {

    companion object {
        const val TABLE = "view_history"
        const val ID = "_id"
        const val QPACK_ID = "qpack_id"
        const val VIEWED_CARD_IDS = "viewed_card_ids"
        const val TODO_CARD_IDS = "todo_card_ids"
        const val ERROR_CARD_IDS = "error_card_ids"
        const val NEW_FAVS_CARD_IDS = "new_favs_card_ids"
        const val REST_CARD_COUNT = "rest_card_count"
        const val LAST_VIEW_DATE = "last_view_date"
    }
}