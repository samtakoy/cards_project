package ru.samtakoy.features.card.data.mapper

import ru.samtakoy.features.card.data.CardWithTagsEntity
import ru.samtakoy.features.card.domain.model.CardWithTags
import ru.samtakoy.features.tag.data.mapper.TagEntityMapper
import javax.inject.Inject

internal interface CardWithTagsEntityMapper {
    fun mapToDomain(entity: CardWithTagsEntity): CardWithTags
}

internal class CardWithTagsEntityMapperImpl @Inject constructor(
    private val cardMapper: CardEntityMapper,
    private val tagMapper: TagEntityMapper
) : CardWithTagsEntityMapper {
    override fun mapToDomain(entity: CardWithTagsEntity): CardWithTags {
        return CardWithTags(
            card = cardMapper.mapToDomain(entity.card),
            tags = entity.tags.map(tagMapper::mapToDomain)
        )
    }
}