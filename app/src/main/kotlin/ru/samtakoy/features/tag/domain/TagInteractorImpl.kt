package ru.samtakoy.features.tag.domain

import ru.samtakoy.domain.cardtag.Tag
import ru.samtakoy.domain.cardtag.TagInteractor
import ru.samtakoy.data.cardtag.TagsRepository
import javax.inject.Inject

class TagInteractorImpl @Inject constructor(
    private val tagsRepository: TagsRepository
) : TagInteractor {
    override fun addTag(tag: Tag): Long {
        return tagsRepository.addTag(tag)
    }

    override fun getTag(id: Long): Tag {
        return tagsRepository.getTag(id)
    }

    override fun getAllTags(): List<Tag> {
        return tagsRepository.getAllTags()
    }

    override fun buildTagMap(): Map<String, Tag> {
        return tagsRepository.buildTagMap()
    }

    override fun deleteAllTagsFromCard(cardId: Long) {
        tagsRepository.deleteAllTagsFromCard(cardId)
    }

    override fun addCardTags(cardId: Long, tagIds: List<Long>) {
        tagsRepository.addCardTags(cardId, tagIds)
    }
}