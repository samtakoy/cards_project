package ru.samtakoy.presentation.cards.screens.view.vm.mapper

import org.jetbrains.compose.resources.getString
import ru.samtakoy.common.utils.coroutines.SuspendLazy
import ru.samtakoy.presentation.cards.view.model.CardViewMode
import ru.samtakoy.presentation.base.model.AnyUiId
import ru.samtakoy.presentation.core.design_system.button.usual.MyButtonUiModel
import ru.samtakoy.presentation.utils.asAnnotated
import ru.samtakoy.resources.Res
import ru.samtakoy.resources.cards_view_next_btn
import ru.samtakoy.resources.cards_view_previous_btn
import ru.samtakoy.resources.cards_view_view_answer_btn

internal interface QuestionButtonsMapper {
    suspend fun map(viewMode: CardViewMode): List<MyButtonUiModel>

    companion object {
        val IdPrevCardBtn = AnyUiId()
        val IdNextCardBtn = AnyUiId()
        val IdViewAnswerBtn = AnyUiId()
    }
}

internal class QuestionButtonsMapperImpl : QuestionButtonsMapper {

    override suspend fun map(viewMode: CardViewMode): List<MyButtonUiModel> {
        return buildList {
            mapPrevCardButton(viewMode)?.let { add(it) }
            mapViewAnswerButton(viewMode)?.let { add(it) }
            mapNextCardButton(viewMode)?.let { add(it) }
        }
    }

    private suspend fun mapPrevCardButton(viewMode: CardViewMode): MyButtonUiModel? {
        return when (viewMode) {
            CardViewMode.LEARNING,
            CardViewMode.REPEATING -> prevButton.getValue()
            CardViewMode.REPEATING_FAST -> null
        }
    }

    private suspend fun mapViewAnswerButton(viewMode: CardViewMode): MyButtonUiModel? {
        return viewAnswerButton.getValue()
    }

    private suspend fun mapNextCardButton(viewMode: CardViewMode): MyButtonUiModel? {
        return when (viewMode) {
            CardViewMode.LEARNING,
            CardViewMode.REPEATING -> null
            CardViewMode.REPEATING_FAST -> nextButton.getValue()
        }
    }

    private val prevButton = SuspendLazy {
        MyButtonUiModel(
            id = QuestionButtonsMapper.IdPrevCardBtn,
            text = getString(Res.string.cards_view_previous_btn).asAnnotated()
        )
    }

    private val viewAnswerButton = SuspendLazy {
        MyButtonUiModel(
            id = QuestionButtonsMapper.IdViewAnswerBtn,
            text = getString(Res.string.cards_view_view_answer_btn).asAnnotated()
        )
    }

    private val nextButton = SuspendLazy {
        MyButtonUiModel(
            id = QuestionButtonsMapper.IdNextCardBtn,
            text = getString(Res.string.cards_view_next_btn).asAnnotated()
        )
    }
}