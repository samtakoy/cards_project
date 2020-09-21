package ru.samtakoy.core.data.local.database.room.entities.other

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ru.samtakoy.core.data.local.database.room.entities.CardEntity
import ru.samtakoy.core.data.local.database.room.entities.CardTagEntity
import ru.samtakoy.core.data.local.database.room.entities.TagEntity
import java.util.*

class CardWithTags(

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
        val tags: MutableList<TagEntity>
) {

    companion object {

        fun createEmptyTags(): MutableList<TagEntity> {
            return ArrayList<TagEntity>()
        }

        fun initNew(qPackId: Long, question: String, answer: String, comment: String): CardWithTags {

            return CardWithTags(
                    CardEntity.initNew(qPackId, question, answer, comment),
                    createEmptyTags()
            )
        }
    }

    fun addTag(tag: TagEntity) {
        tags.add(tag)
    }

    fun addTagsFrom(tagList: List<TagEntity>) {
        for (tag in tagList) {
            addTag(tag)
        }
    }
}