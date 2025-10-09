package ru.samtakoy.features.tag.data

import ru.samtakoy.features.tag.domain.Tag

interface TagsRepository {

    fun addTag(tag: Tag): Long
    fun getTag(id: Long): Tag
    fun getAllTags(): List<Tag>
    fun buildTagMap(): Map<String, Tag>

    fun deleteAllTagsFromCard(cardId: Long)
    fun addCardTags(cardId: Long, tagIds: List<Long>)
}