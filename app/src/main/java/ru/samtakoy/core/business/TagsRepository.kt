package ru.samtakoy.core.business

import ru.samtakoy.core.database.room.entities.TagEntity
import java.util.*

interface TagsRepository {

    fun addTag(tag: TagEntity): Long
    fun getTag(id: Long): TagEntity
    fun getAllTags(): List<TagEntity>
    fun buildTagMap(): HashMap<String, TagEntity>

    fun deleteAllTagsFromCard(cardId: Long)
    fun addCardTags(cardId: Long, tags: List<TagEntity>)
}