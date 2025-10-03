package ru.samtakoy.core.presentation.qpack.info.mapper

import ru.samtakoy.core.app.utils.asAnnotated
import ru.samtakoy.core.data.local.database.room.entities.CardEntity
import ru.samtakoy.core.presentation.design_system.base.model.LongUiId
import ru.samtakoy.core.presentation.qpack.info.FastCardUiModel
import javax.inject.Inject

internal interface FastCardUiModelMapper {
    fun map(card: CardEntity): FastCardUiModel
}

internal class FastCardUiModelMapperImpl @Inject constructor(): FastCardUiModelMapper {
    override fun map(card: CardEntity): FastCardUiModel {
        return FastCardUiModel(
            id = LongUiId(card.id),
            question = card.question.asAnnotated(),
            answer = card.answer.asAnnotated()
        )
    }
}