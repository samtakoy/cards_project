package ru.samtakoy.core.presentation.qpack.info.mapper

import ru.samtakoy.core.presentation.design_system.base.model.LongUiId
import ru.samtakoy.core.presentation.qpack.info.FastCardUiModel
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