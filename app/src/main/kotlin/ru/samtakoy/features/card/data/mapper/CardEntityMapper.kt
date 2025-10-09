package ru.samtakoy.features.card.data.mapper

import ru.samtakoy.features.card.data.CardEntity
import ru.samtakoy.features.card.data.CardWithTagsEntity
import ru.samtakoy.features.card.domain.model.Card
import ru.samtakoy.features.card.domain.model.CardWithTags
import javax.inject.Inject

internal interface CardEntityMapper {
    fun mapToEntity(model: Card): CardEntity
    fun mapToDomain(entity: CardEntity): Card
}

internal class CardEntityMapperImpl @Inject constructor() : CardEntityMapper {
    override fun mapToEntity(model: Card): CardEntity {
        return CardEntity(
            id = model.id,
            qPackId = model.qPackId,
            question = model.question,
            answer = model.answer,
            aImages = model.aImages,
            comment = model.comment,
            views = 0,
            errors = 0,
            favorite = model.favorite
        )
    }

    override fun mapToDomain(entity: CardEntity): Card {
        return Card(
            id = entity.id,
            qPackId = entity.qPackId,
            question = entity.question,
            answer = entity.answer,
            aImages = entity.aImages,
            comment = entity.comment,
            favorite = entity.favorite
        )
    }
}