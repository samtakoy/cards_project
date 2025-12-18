package ru.samtakoy.domain.cardtag

interface TagInteractor {
    suspend fun addTag(tag: Tag): Long
    suspend fun getTag(id: Long): Tag
    suspend fun getAllTags(): List<Tag>
    suspend fun buildTagMap(): ConcurrentTagMap
    suspend fun addTags(tags: List<Tag>): List<Tag>
    suspend fun deleteAllTagsFromCard(cardId: Long)
    suspend fun addCardTags(cardId: Long, tagIds: List<Long>)
}