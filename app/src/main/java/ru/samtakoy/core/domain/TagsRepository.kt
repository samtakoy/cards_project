package ru.samtakoy.core.domain

import ru.samtakoy.core.data.local.database.room.entities.TagEntity
import java.util.*

interface TagsRepository {

    fun addTag(tag: TagEntity): Long
    fun getTag(id: Long): TagEntity
    fun getAllTags(): List<TagEntity>
    fun buildTagMap(): HashMap<String, TagEntity>

    fun deleteAllTagsFromCard(cardId: Long)
    fun addCardTags(cardId: Long, tags: List<TagEntity>)
}