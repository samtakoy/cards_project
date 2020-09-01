package ru.samtakoy.core.database.room.entities.other

import androidx.room.Embedded
import androidx.room.Relation
import ru.samtakoy.core.database.room.entities.CardEntity
import ru.samtakoy.core.database.room.entities.QPackEntity

class QPackWithCardIds(

        @Embedded
        val qPack: QPackEntity,

        @Relation(
                parentColumn = QPackEntity._id,
                entityColumn = CardEntity._qpack_id,
                entity = CardEntity::class,
                projection = [CardEntity._id]
        )
        val cardIds: List<Long>
) {


    val id: Long
        get() = qPack.id

    val cardCount: Int
        get() = cardIds.size

}