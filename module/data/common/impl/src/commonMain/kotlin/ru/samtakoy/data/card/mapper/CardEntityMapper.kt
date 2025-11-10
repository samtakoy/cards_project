package ru.samtakoy.data.card.mapper

import ru.samtakoy.data.card.model.CardEntity
import ru.samtakoy.domain.card.domain.model.Card

internal interface CardEntityMapper {
    fun mapToEntity(model: Card): CardEntity
    fun mapToDomain(entity: CardEntity): Card
}

internal class CardEntityMapperImpl() : CardEntityMapper {
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