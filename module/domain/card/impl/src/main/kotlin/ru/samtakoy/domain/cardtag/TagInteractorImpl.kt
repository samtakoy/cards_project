package ru.samtakoy.domain.cardtag

import ru.samtakoy.data.cardtag.TagsRepository

internal class TagInteractorImpl(
    private val tagsRepository: TagsRepository
) : TagInteractor {
    override suspend fun addTag(tag: Tag): Long {
        return tagsRepository.addTag(tag)
    }

    override suspend fun getTag(id: Long): Tag {
        return tagsRepository.getTag(id)
    }

    override suspend fun getAllTags(): List<Tag> {
        return tagsRepository.getAllTags()
    }

    override suspend fun buildTagMap(): ConcurrentTagMap {
        return ConcurrentTagMap().also {
            it.addTags(tagsRepository.getAllTags())
        }
    }

    override suspend fun addTags(tags: List<Tag>): List<Tag> {
        return tagsRepository.addTags(tags)
    }

    override suspend fun deleteAllTagsFromCard(cardId: Long) {
        tagsRepository.deleteAllTagsFromCard(cardId)
    }

    override suspend fun addCardTags(cardId: Long, tagIds: List<Long>) {
        tagsRepository.addCardTags(cardId, tagIds)
    }
}