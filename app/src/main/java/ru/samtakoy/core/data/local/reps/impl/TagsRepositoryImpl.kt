package ru.samtakoy.core.data.local.reps.impl

import ru.samtakoy.core.data.local.database.room.MyRoomDb
import ru.samtakoy.core.data.local.database.room.entities.CardTagEntity
import ru.samtakoy.core.data.local.database.room.entities.TagEntity
import ru.samtakoy.core.data.local.reps.TagsRepository
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class TagsRepositoryImpl @Inject constructor(
        val db: MyRoomDb
) : TagsRepository {


    override fun addTag(tag: TagEntity): Long {
        val tagId: Long = db.tagDao().addTag(tag)
        tag.id = tagId
        return tagId
    }

    override fun getTag(id: Long): TagEntity =
            db.tagDao().getTag(id)

    override fun getAllTags(): List<TagEntity> =
            db.tagDao().getAllTags()


    override fun buildTagMap(): HashMap<String, TagEntity> {
        val tags: List<TagEntity> = getAllTags()
        val result = HashMap<String, TagEntity>()
        for (tag in tags) {
            result[tag.key] = tag
        }
        return result
    }

    override fun deleteAllTagsFromCard(cardId: Long) {
        db.cardTagDao().deleteAllFromCard(cardId)
    }

    override fun addCardTags(cardId: Long, tags: List<TagEntity>) {
        val cardTags: MutableList<CardTagEntity> = ArrayList<CardTagEntity>(tags.size);
        for (tag: TagEntity in tags) {
            cardTags.add(CardTagEntity(cardId, tag.id))
        }
        db.cardTagDao().addTags(cardTags)
    }
}