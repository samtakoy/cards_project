package ru.samtakoy.data.card.mapper

import ru.samtakoy.data.card.model.CardWithTagsEntity
import ru.samtakoy.data.cardtag.mapper.TagEntityMapper
import ru.samtakoy.domain.card.domain.model.CardWithTags

internal interface CardWithTagsEntityMapper {
    fun mapToDomain(entity: CardWithTagsEntity): CardWithTags
}

internal class CardWithTagsEntityMapperImpl(
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