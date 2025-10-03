package ru.samtakoy.core.data.local.database.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import ru.samtakoy.core.data.local.database.room.entities.CardTagEntity.Companion._card_id
import ru.samtakoy.core.data.local.database.room.entities.CardTagEntity.Companion._tag_id

@Entity(
        tableName = CardTagEntity.table,
        foreignKeys = [
            ForeignKey(
                    entity = CardEntity::class,
                    parentColumns = ["_id"],
                    childColumns = [_card_id],
                    onDelete = ForeignKey.CASCADE
            ),
            ForeignKey(
                    entity = TagEntity::class,
                    parentColumns = ["_id"],
                    childColumns = [_tag_id],
                    onDelete = ForeignKey.RESTRICT
            )
        ],
        primaryKeys = [_card_id, _tag_id]
)
class CardTagEntity(
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