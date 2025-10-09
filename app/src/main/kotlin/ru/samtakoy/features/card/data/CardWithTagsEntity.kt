package ru.samtakoy.features.card.data

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ru.samtakoy.features.tag.data.CardTagEntity
import ru.samtakoy.features.tag.data.TagEntity

data class CardWithTagsEntity(

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