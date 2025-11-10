package ru.samtakoy.data.cardtag.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import ru.samtakoy.data.card.model.CardEntity

@Entity(
        tableName = CardTagEntity.table,
        foreignKeys = [
            ForeignKey(
                entity = CardEntity::class,
                parentColumns = ["_id"],
                childColumns = [CardTagEntity.Companion._card_id],
                onDelete = ForeignKey.CASCADE
            ),
            ForeignKey(
                entity = TagEntity::class,
                parentColumns = ["_id"],
                childColumns = [CardTagEntity.Companion._tag_id],
                onDelete = ForeignKey.RESTRICT
            )
        ],
        primaryKeys = [CardTagEntity.Companion._card_id, CardTagEntity.Companion._tag_id]
)
internal class CardTagEntity(
        @ColumnInfo(name = _card_id)
        val cardId: Long,
        @ColumnInfo(name = _tag_id, index = true)
        val tagId: Long
) {

    companion object {
        const val table = "cards_tags"

        const val _card_id = "card_id"
        const val _tag_id = "tag_id"
    }

}