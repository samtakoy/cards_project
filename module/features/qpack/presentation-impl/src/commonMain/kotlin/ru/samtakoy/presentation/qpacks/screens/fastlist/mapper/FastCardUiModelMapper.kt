package ru.samtakoy.presentation.qpacks.screens.fastlist.mapper

import ru.samtakoy.presentation.core.design_system.base.model.LongUiId
import ru.samtakoy.presentation.qpacks.screens.fastlist.model.FastCardUiModel
import ru.samtakoy.domain.card.domain.model.Card
import ru.samtakoy.presentation.utils.asAnnotated

internal interface FastCardUiModelMapper {
    fun map(card: Card): FastCardUiModel
}

internal class FastCardUiModelMapperImpl(): FastCardUiModelMapper {
    override fun map(card: Card): FastCardUiModel {
        return FastCardUiModel(
            id = LongUiId(card.id),
            question = card.question.asAnnotated(),
            answer = card.answer.asAnnotated()
        )
    }
}