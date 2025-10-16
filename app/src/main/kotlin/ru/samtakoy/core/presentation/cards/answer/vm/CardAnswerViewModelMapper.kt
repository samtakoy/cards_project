package ru.samtakoy.core.presentation.cards.answer.vm

import ru.samtakoy.R
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.core.presentation.cards.answer.vm.CardAnswerViewModelMapper.Companion.BACK_BTN_ID
import ru.samtakoy.core.presentation.cards.answer.vm.CardAnswerViewModelMapper.Companion.IS_FAVORITE_ID
import ru.samtakoy.core.presentation.cards.answer.vm.CardAnswerViewModelMapper.Companion.NEXT_CARD_BTN_ID
import ru.samtakoy.core.presentation.cards.answer.vm.CardAnswerViewModelMapper.Companion.WRONG_BTN_ID
import ru.samtakoy.core.presentation.cards.types.CardViewMode
import ru.samtakoy.core.presentation.design_system.base.model.LongUiId
import ru.samtakoy.core.presentation.design_system.button.MyButtonModel
import ru.samtakoy.core.presentation.design_system.selectable_item.MySelectableItemModel
import ru.samtakoy.presentation.utils.asAnnotated

interface CardAnswerViewModelMapper {
    fun mapBackButton(viewMode: CardViewMode): MyButtonModel?
    fun mapWrongButton(viewMode: CardViewMode): MyButtonModel?
    fun mapNextCardButton(viewMode: CardViewMode): MyButtonModel?
    fun mapFavoriteItem(isFavorite: Boolean): MySelectableItemModel

    companion object {
        val BACK_BTN_ID = LongUiId(1)
        val WRONG_BTN_ID = LongUiId(2)
        val NEXT_CARD_BTN_ID = LongUiId(3)
        val IS_FAVORITE_ID = LongUiId(4)
    }
}

internal class CardAnswerViewModelMapperImpl(
    private val resources: Resources
): CardAnswerViewModelMapper {
    override fun mapBackButton(viewMode: CardViewMode): MyButtonModel? {
        return when (viewMode) {
            CardViewMode.LEARNING -> {
                MyButtonModel(
                    id = BACK_BTN_ID,
                    text = resources.getString(R.string.cards_view_back_from_answer_btn).asAnnotated()
                )
            }
            CardViewMode.REPEATING,
            CardViewMode.REPEATING_FAST -> null
        }
    }

    override fun mapWrongButton(viewMode: CardViewMode): MyButtonModel? {
        return when (viewMode) {
            CardViewMode.LEARNING -> null
            CardViewMode.REPEATING,
            CardViewMode.REPEATING_FAST -> {
                MyButtonModel(
                    id = WRONG_BTN_ID,
                    text = resources.getString(R.string.cards_view_wrong_btn).asAnnotated()
                )
            }
        }
    }

    override fun mapNextCardButton(viewMode: CardViewMode): MyButtonModel? {
        return when (viewMode) {
            CardViewMode.LEARNING,
            CardViewMode.REPEATING,
            CardViewMode.REPEATING_FAST -> {
                MyButtonModel(
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