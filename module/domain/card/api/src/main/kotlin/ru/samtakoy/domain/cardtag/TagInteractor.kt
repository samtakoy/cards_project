package ru.samtakoy.domain.cardtag

interface TagInteractor {
    fun addTag(tag: Tag): Long
    fun getTag(id: Long): Tag
    fun getAllTags(): List<Tag>
    fun buildTagMap(): Map<String, Tag>

    fun deleteAllTagsFromCard(cardId: Long)
    fun addCardTags(cardId: Long, tagIds: List<Long>)
}