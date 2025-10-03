package ru.samtakoy.core.data.local.database.room.entities.other

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ru.samtakoy.core.data.local.database.room.entities.CardEntity
import ru.samtakoy.core.data.local.database.room.entities.CardTagEntity
import ru.samtakoy.core.data.local.database.room.entities.TagEntity
import kotlin.String
import java.util.*

data class CardWithTags(

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
) {

    companion object {

        fun createEmptyTags(): MutableList<TagEntity> {
            return ArrayList<TagEntity>()
        }

        fun initNew(
            cardId: Long,
            qPackId: Long,
            question: String,
            answer: String,
            aImages: List<String>,
            comment: String,
            tags: List<TagEntity>
        ): CardWithTags {
            return CardWithTags(
                card = CardEntity.initNew(
                    id = cardId,
                    qPackId = qPackId,
                    question = question,
                    answer = answer,
                    aImages = aImages,
                    comment = comment
                ),
                tags = tags
            )
        }
    }
}