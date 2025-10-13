package ru.samtakoy.data.card.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ru.samtakoy.data.cardtag.model.CardTagEntity
import ru.samtakoy.data.cardtag.model.TagEntity

internal data class CardWithTagsEntity(

        @Embedded
        val card: CardEntity,
        @Relation(
                parentColumn = CardEntity._id,
                entityColumn = TagEntity._id,
                associateBy = Junction(
                        CardTagEntity::class,
                        parentColumn = CardTagEntity._card_id,
                        entityColumn = CardTagEntity._tag_id)
        )
        val tags: List<TagEntity>
)