package ru.samtakoy.presentation.cards.screens.view.vm.mapper

import ru.samtakoy.common.resources.Resources
import ru.samtakoy.presentation.cards.impl.R
import ru.samtakoy.presentation.cards.view.model.CardViewMode
import ru.samtakoy.presentation.core.design_system.base.model.AnyUiId
import ru.samtakoy.presentation.core.design_system.button.usual.MyButtonUiModel
import ru.samtakoy.presentation.utils.asAnnotated

internal interface QuestionButtonsMapper {
    fun map(viewMode: CardViewMode): List<MyButtonUiModel>

    companion object {
        val IdPrevCardBtn = AnyUiId()
        val IdNextCardBtn = AnyUiId()
        val IdViewAnswerBtn = AnyUiId()
    }
}

internal class QuestionButtonsMapperImpl(
    private val resources: Resources
) : QuestionButtonsMapper {

    override fun map(viewMode: CardViewMode): List<MyButtonUiModel> {
        return buildList {
            mapPrevCardButton(viewMode)?.let { add(it) }
            mapViewAnswerButton(viewMode)?.let { add(it) }
            mapNextCardButton(viewMode)?.let { add(it) }
        }
    }

    private fun mapPrevCardButton(viewMode: CardViewMode): MyButtonUiModel? {
        return when (viewMode) {
            CardViewMode.LEARNING,
            CardViewMode.REPEATING -> prevButton
            CardViewMode.REPEATING_FAST -> null
        }
    }

    private fun mapViewAnswerButton(viewMode: CardViewMode): MyButtonUiModel? {
        return viewAnswerButton
    }

    private fun mapNextCardButton(viewMode: CardViewMode): MyButtonUiModel? {
        return when (viewMode) {
            CardViewMode.LEARNING,
            CardViewMode.REPEATING -> null
            CardViewMode.REPEATING_FAST -> nextButton
        }
    }

    private val prevButton: MyButtonUiModel by lazy {
        MyButtonUiModel(
            id = QuestionButtonsMapper.IdPrevCardBtn,
            text = resources.getString(R.string.cards_view_previous_btn).asAnnotated()
        )
    }

    private val viewAnswerButton: MyButtonUiModel by lazy {
        MyButtonUiModel(
            id = QuestionButtonsMapper.IdViewAnswerBtn,
            text = resources.getString(R.string.cards_view_view_answer_btn).asAnnotated()
        )
    }

    private val nextButton: MyButtonUiModel by lazy {
        MyButtonUiModel(
            id = QuestionButtonsMapper.IdNextCardBtn,
            text = resources.getString(R.string.cards_view_next_btn).asAnnotated()
        )
    }
}