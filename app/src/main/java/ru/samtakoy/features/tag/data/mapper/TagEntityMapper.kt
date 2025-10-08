package ru.samtakoy.features.tag.data.mapper

import ru.samtakoy.features.tag.data.TagEntity
import ru.samtakoy.features.tag.domain.Tag
import javax.inject.Inject

internal interface TagEntityMapper {
    fun mapToEntity(model: Tag): TagEntity
    fun mapToDomain(entity: TagEntity): Tag
}

internal class TagEntityMapperImpl @Inject constructor() : TagEntityMapper {
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