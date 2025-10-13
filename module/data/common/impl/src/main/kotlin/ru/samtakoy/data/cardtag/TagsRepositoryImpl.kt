package ru.samtakoy.data.cardtag

import ru.samtakoy.data.cardtag.mapper.TagEntityMapper
import ru.samtakoy.data.cardtag.model.CardTagEntity
import ru.samtakoy.domain.cardtag.Tag
import ru.samtakoy.domain.cardtag.toStringKey
import javax.inject.Inject

internal class TagsRepositoryImpl @Inject constructor(
    private val tagDao: TagDao,
    private val cardTagDao: CardTagDao,
    private val tagMapper: TagEntityMapper
) : TagsRepository {

    override fun addTag(tag: Tag): Long {
        return tagDao.addTag(tagMapper.mapToEntity(tag))
    }

    override fun getTag(id: Long): Tag {
        return tagDao.getTag(id).let(tagMapper::mapToDomain)
    }

    override fun getAllTags(): List<Tag> {
        return tagDao.getAllTags().map(tagMapper::mapToDomain)
    }

    override fun buildTagMap(): Map<String, Tag> {
        val tags: List<Tag> = getAllTags()
        val result = mutableMapOf<String, Tag>()
        for (tag in tags) {
            result[tagToKey(tag)] = tag
        }
        return result
    }

    override fun deleteAllTagsFromCard(cardId: Long) {
        cardTagDao.deleteAllFromCard(cardId)
    }

    override fun addCardTags(cardId: Long, tagIds: List<Long>) {
        cardTagDao.addTags(
            tagIds.map {
                CardTagEntity(cardId, it)
            }
        )
    }

    private fun tagToKey(tag: Tag): String {
        return tag.toStringKey()
    }
}