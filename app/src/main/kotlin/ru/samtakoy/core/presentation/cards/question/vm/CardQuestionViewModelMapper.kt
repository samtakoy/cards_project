package ru.samtakoy.core.presentation.cards.question.vm

import ru.samtakoy.R
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.core.presentation.cards.question.vm.CardQuestionViewModelMapper.Companion.IS_FAVORITE_ID
import ru.samtakoy.core.presentation.cards.question.vm.CardQuestionViewModelMapper.Companion.NEXT_CARD_BTN_ID
import ru.samtakoy.core.presentation.cards.question.vm.CardQuestionViewModelMapper.Companion.PREV_CARD_BTN_ID
import ru.samtakoy.core.presentation.cards.question.vm.CardQuestionViewModelMapper.Companion.VIEW_ANSWER_BTN_ID
import ru.samtakoy.core.presentation.cards.types.CardViewMode
import ru.samtakoy.presentation.core.design_system.base.model.LongUiId
import ru.samtakoy.presentation.core.design_system.button.MyButtonUiModel
import ru.samtakoy.presentation.core.design_system.selectable_item.MySelectableItemModel
import ru.samtakoy.presentation.utils.asAnnotated

interface CardQuestionViewModelMapper {
    fun mapPrevCardButton(viewMode: CardViewMode): MyButtonUiModel?
    fun mapViewAnswerButton(viewMode: CardViewMode): MyButtonUiModel?
    fun mapNextCardButton(viewMode: CardViewMode): MyButtonUiModel?
    fun mapFavoriteItem(isFavorite: Boolean): MySelectableItemModel

    companion object {
        val PREV_CARD_BTN_ID = LongUiId(1)
        val NEXT_CARD_BTN_ID = LongUiId(2)
        val VIEW_ANSWER_BTN_ID = LongUiId(3)
        val IS_FAVORITE_ID = LongUiId(4)
    }
}

class CardQuestionViewModelMapperImpl(
    private val resources: Resources
): CardQuestionViewModelMapper {

    override fun mapPrevCardButton(viewMode: CardViewMode): MyButtonUiModel? {
        return when (viewMode) {
            CardViewMode.LEARNING,
            CardViewMode.REPEATING -> {
                MyButtonUiModel(
                    id = PREV_CARD_BTN_ID,
                    text = resources.getString(R.string.cards_view_previous_btn).asAnnotated()
                )
            }
            CardViewMode.REPEATING_FAST -> null
        }
    }

    override fun mapViewAnswerButton(viewMode: CardViewMode): MyButtonUiModel? {
        return MyButtonUiModel(
            id = VIEW_ANSWER_BTN_ID,
            text = resources.getString(R.string.cards_view_view_answer_btn).asAnnotated()
        )
    }

    override fun mapNextCardButton(viewMode: CardViewMode): MyButtonUiModel? {
        return when (viewMode) {
            CardViewMode.LEARNING,
            CardViewMode.REPEATING -> null
            CardViewMode.REPEATING_FAST -> {
                MyButtonUiModel(
                    id = NEXT_CARD_BTN_ID,
                    text = resources.getString(R.string.cards_view_next_btn).asAnnotated()
                )
            }
        }
    }

    override fun mapFavoriteItem(isFavorite: Boolean): MySelectableItemModel {
        return MySelectableItemModel(
            id = IS_FAVORITE_ID,
            text = resources.getString(R.string.cards_view_favorite_box).asAnnotated(),
            isChecked = isFavorite,
            isEnabled = true,
            contentDescription = resources.getString(R.string.cards_view_favorite_box)
        )
    }
}