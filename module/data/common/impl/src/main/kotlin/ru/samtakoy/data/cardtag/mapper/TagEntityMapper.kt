package ru.samtakoy.data.cardtag.mapper

import ru.samtakoy.data.cardtag.model.TagEntity
import ru.samtakoy.domain.cardtag.Tag

internal interface TagEntityMapper {
    fun mapToEntity(model: Tag): TagEntity
    fun mapToDomain(entity: TagEntity): Tag
}

internal class TagEntityMapperImpl() : TagEntityMapper {
    override fun mapToEntity(model: Tag): TagEntity {
        return TagEntity(
            id = model.id,
            title = model.title
        )
    }

    override fun mapToDomain(entity: TagEntity): Tag {
        return Tag(
            id = entity.id,
            title = entity.title
        )
    }
}