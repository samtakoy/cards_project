package ru.samtakoy.presentation.cards.screens.view.vm.mapper

import ru.samtakoy.common.resources.Resources
import ru.samtakoy.presentation.cards.impl.R
import ru.samtakoy.presentation.cards.view.model.CardViewMode
import ru.samtakoy.presentation.core.design_system.base.model.AnyUiId
import ru.samtakoy.presentation.core.design_system.button.usual.MyButtonUiModel
import ru.samtakoy.presentation.utils.asAnnotated

internal interface AnswerButtonsMapper {
    fun map(viewMode: CardViewMode): List<MyButtonUiModel>

    companion object {
        val IdBackBtn = AnyUiId()
        val IdWrongBtn = AnyUiId()
        val IdNextCardBtn = AnyUiId()
    }
}

internal class AnswerButtonsMapperImpl(
    private val resources: Resources
) : AnswerButtonsMapper {

    override fun map(viewMode: CardViewMode): List<MyButtonUiModel> {
        return buildList {
            mapBackButton(viewMode)?.let { add(it) }
            mapWrongButton(viewMode)?.let { add(it) }
            mapNextCardButton(viewMode)?.let { add(it) }
        }
    }

    private fun mapBackButton(viewMode: CardViewMode): MyButtonUiModel? {
        return when (viewMode) {
            CardViewMode.LEARNING -> backButton
            CardViewMode.REPEATING,
            CardViewMode.REPEATING_FAST -> null
        }
    }

    private fun mapWrongButton(viewMode: CardViewMode): MyButtonUiModel? {
        return when (viewMode) {
            CardViewMode.LEARNING -> null
            CardViewMode.REPEATING,
            CardViewMode.REPEATING_FAST -> wrongButton
        }
    }

    private fun mapNextCardButton(viewMode: CardViewMode): MyButtonUiModel? {
        return when (viewMode) {
            CardViewMode.LEARNING,
            CardViewMode.REPEATING,
            CardViewMode.REPEATING_FAST -> nextButton
        }
    }

    private val backButton: MyButtonUiModel by lazy {
        MyButtonUiModel(
            id = AnswerButtonsMapper.IdBackBtn,
            text = resources.getString(R.string.cards_view_back_from_answer_btn).asAnnotated()
        )
    }

    private val wrongButton: MyButtonUiModel by lazy {
        MyButtonUiModel(
            id = AnswerButtonsMapper.IdWrongBtn,
            text = resources.getString(R.string.cards_view_wrong_btn).asAnnotated()
        )
    }

    private val nextButton: MyButtonUiModel by lazy {
        MyButtonUiModel(
            id = AnswerButtonsMapper.IdNextCardBtn,
            text = resources.getString(R.string.cards_view_next_btn).asAnnotated()
        )
    }
}