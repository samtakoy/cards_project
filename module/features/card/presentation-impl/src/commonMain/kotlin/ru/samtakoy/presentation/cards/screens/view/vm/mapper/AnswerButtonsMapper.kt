package ru.samtakoy.presentation.cards.screens.view.vm.mapper

import org.jetbrains.compose.resources.getString
import ru.samtakoy.common.utils.coroutines.SuspendLazy
import ru.samtakoy.presentation.cards.view.model.CardViewMode
import ru.samtakoy.presentation.base.model.AnyUiId
import ru.samtakoy.presentation.core.design_system.button.usual.MyButtonUiModel
import ru.samtakoy.presentation.utils.asAnnotated
import ru.samtakoy.resources.Res
import ru.samtakoy.resources.cards_view_back_from_answer_btn
import ru.samtakoy.resources.cards_view_next_btn
import ru.samtakoy.resources.cards_view_wrong_btn

internal interface AnswerButtonsMapper {
    suspend fun map(viewMode: CardViewMode): List<MyButtonUiModel>

    companion object {
        val IdBackBtn = AnyUiId()
        val IdWrongBtn = AnyUiId()
        val IdNextCardBtn = AnyUiId()
    }
}

internal class AnswerButtonsMapperImpl : AnswerButtonsMapper {

    override suspend fun map(viewMode: CardViewMode): List<MyButtonUiModel> {
        return buildList {
            mapBackButton(viewMode)?.let { add(it) }
            mapWrongButton(viewMode)?.let { add(it) }
            mapNextCardButton(viewMode)?.let { add(it) }
        }
    }

    private suspend fun mapBackButton(viewMode: CardViewMode): MyButtonUiModel? {
        return when (viewMode) {
            CardViewMode.LEARNING -> backButton.getValue()
            CardViewMode.REPEATING,
            CardViewMode.REPEATING_FAST -> null
        }
    }

    private suspend fun mapWrongButton(viewMode: CardViewMode): MyButtonUiModel? {
        return when (viewMode) {
            CardViewMode.LEARNING -> null
            CardViewMode.REPEATING,
            CardViewMode.REPEATING_FAST -> wrongButton.getValue()
        }
    }

    private suspend fun mapNextCardButton(viewMode: CardViewMode): MyButtonUiModel? {
        return when (viewMode) {
            CardViewMode.LEARNING,
            CardViewMode.REPEATING,
            CardViewMode.REPEATING_FAST -> nextButton.getValue()
        }
    }

    private val backButton = SuspendLazy {
        MyButtonUiModel(
            id = AnswerButtonsMapper.IdBackBtn,
            text = getString(Res.string.cards_view_back_from_answer_btn).asAnnotated()
        )
    }

    private val wrongButton = SuspendLazy {
        MyButtonUiModel(
            id = AnswerButtonsMapper.IdWrongBtn,
            text = getString(Res.string.cards_view_wrong_btn).asAnnotated()
        )
    }

    private val nextButton = SuspendLazy {
        MyButtonUiModel(
            id = AnswerButtonsMapper.IdNextCardBtn,
            text = getString(Res.string.cards_view_next_btn).asAnnotated()
        )
    }
}