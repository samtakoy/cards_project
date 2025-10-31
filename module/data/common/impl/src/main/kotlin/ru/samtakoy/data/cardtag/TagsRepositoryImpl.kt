package ru.samtakoy.data.cardtag

import ru.samtakoy.data.cardtag.mapper.TagEntityMapper
import ru.samtakoy.data.cardtag.model.CardTagEntity
import ru.samtakoy.domain.cardtag.Tag
import ru.samtakoy.domain.cardtag.toStringKey

internal class TagsRepositoryImpl(
    private val tagDao: TagDao,
    private val cardTagDao: CardTagDao,
    private val tagMapper: TagEntityMapper
) : TagsRepository {

    override suspend fun addTag(tag: Tag): Long {
        return tagDao.addTag(tagMapper.mapToEntity(tag))
    }

    override suspend fun getTag(id: Long): Tag {
        return tagDao.getTag(id).let(tagMapper::mapToDomain)
    }

    override suspend fun getAllTags(): List<Tag> {
        return tagDao.getAllTags().map(tagMapper::mapToDomain)
    }

    override suspend fun addTags(tags: List<Tag>): List<Tag> {
        val ids = tagDao.addTags(tags.map(tagMapper::mapToEntity))
        return tagDao.getAllById(ids).map(tagMapper::mapToDomain)
    }

    override suspend fun deleteAllTagsFromCard(cardId: Long) {
        cardTagDao.deleteAllFromCard(cardId)
    }

    override suspend fun addCardTags(cardId: Long, tagIds: List<Long>) {
        cardTagDao.addTags(
            tagIds.map {
                CardTagEntity(cardId, it)
            }
        )
    }
}