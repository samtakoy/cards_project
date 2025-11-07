package ru.samtakoy.presentation.cards.screens.view.vm.mapper

import ru.samtakoy.common.resources.Resources
import ru.samtakoy.common.utils.R as RUtils
import ru.samtakoy.presentation.cards.view.model.CardViewMode
import ru.samtakoy.presentation.cards.screens.view.vm.CardsViewViewModel.State
import ru.samtakoy.presentation.cards.screens.view.vm.CardsViewViewModelImpl
import ru.samtakoy.presentation.cards.screens.view.vm.CardsViewViewModelImpl.DataState
import ru.samtakoy.presentation.utils.asA

internal interface CardsViewViewStateMapper {

    fun mapStateType(
        dataState: DataState,
        viewMode: CardViewMode,
        viewHistoryItemId: Long
    ): State.Type
}

internal class CardsViewViewStateMapperImpl(
    private val resources: Resources
): CardsViewViewStateMapper {

    override fun mapStateType(
        dataState: DataState,
        viewMode: CardViewMode,
        viewHistoryItemId: Long
    ): State.Type {
        val cardStateType = dataState.cardInfo?.state?.type
        return when (cardStateType) {
            null, CardsViewViewModelImpl.CurCardState.Type.NOT_INITIALIZED -> {
                State.Type.Initialization
            }
            CardsViewViewModelImpl.CurCardState.Type.QUESTION,
            CardsViewViewModelImpl.CurCardState.Type.ANSWER -> {
                val card = dataState.cardInfo.card
                val isOnAnswer = cardStateType == CardsViewViewModelImpl.CurCardState.Type.ANSWER
                if (card == null) {
                    // Такой стейт невозможен по логике программы, только если карточку не удалось запросить из БД
                    State.Type.Error(errorText = resources.getString(RUtils.string.common_err_message))
                } else {
                    val totalCount = dataState.allCardIds.size
                    val curTrueCardIndex = dataState.allCardIds.indexOf(card.id)
                    State.Type.Card(
                        cardsCountTitle = "${curTrueCardIndex + 1}/$totalCount".asA(),
                        cardIndex = curTrueCardIndex * 2 + (if (isOnAnswer) 1 else 0),
                    )
                }
            }
        }
    }
}