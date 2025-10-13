package ru.samtakoy.data.cardtag

import ru.samtakoy.domain.cardtag.Tag

interface TagsRepository {

    fun addTag(tag: Tag): Long
    fun getTag(id: Long): Tag
    fun getAllTags(): List<Tag>
    fun buildTagMap(): Map<String, Tag>

    fun deleteAllTagsFromCard(cardId: Long)
    fun addCardTags(cardId: Long, tagIds: List<Long>)
}