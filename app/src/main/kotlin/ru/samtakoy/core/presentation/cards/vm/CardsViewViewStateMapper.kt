package ru.samtakoy.core.presentation.cards.vm

import ru.samtakoy.R
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.core.presentation.cards.types.BackupInfo
import ru.samtakoy.core.presentation.cards.types.CardViewMode
import ru.samtakoy.core.presentation.cards.vm.CardsViewViewModel.State
import ru.samtakoy.core.presentation.cards.vm.CardsViewViewModelImpl.DataState

internal interface CardsViewViewStateMapper {
    fun mapStateType(
        dataState: DataState?,
        viewMode: CardViewMode
    ): State.Type
}

internal class CardsViewViewStateMapperImpl(
    private val resources: Resources
): CardsViewViewStateMapper {
    override fun mapStateType(
        dataState: DataState?,
        viewMode: CardViewMode
    ): State.Type {
        val cardStateType = dataState?.cardInfo?.state?.type
        return when (cardStateType) {
            null, CardsViewViewModelImpl.CurCardState.Type.NOT_INITIALIZED -> {
                State.Type.Initialization
            }
            CardsViewViewModelImpl.CurCardState.Type.RESULTS -> {
                State.Type.Results(
                    viewItemId = dataState.viewHistoryItem.id
                )
            }
            CardsViewViewModelImpl.CurCardState.Type.QUESTION,
            CardsViewViewModelImpl.CurCardState.Type.ANSWER -> {
                val card = dataState.cardInfo.card
                val viewHistoryItem = dataState.viewHistoryItem
                val isOnAnswer = cardStateType == CardsViewViewModelImpl.CurCardState.Type.ANSWER
                if (card == null) {
                    // Такой стейт невозможен по логике программы, только если карточку не удалось запросить из БД
                    State.Type.Error(errorText = resources.getString(R.string.common_err_message))
                } else {
                    val allCardIds = viewHistoryItem.viewedCardIds + viewHistoryItem.todoCardIds
                    State.Type.Card(
                        qPackId = card.qPackId,
                        cardId = card.id,
                        cardIndex = allCardIds.indexOf(card.id),
                        cardsCount = allCardIds.size,
                        onAnswer = isOnAnswer,
                        hasRevertButton = resolveHasRevertButton(
                            backupInfo = dataState.cardInfo.backup,
                            isOnAnswer = isOnAnswer
                        )
                    )
                }
            }
        }
    }

    private fun resolveHasRevertButton(
        backupInfo: BackupInfo?,
        isOnAnswer: Boolean
    ): Boolean {
        return backupInfo != null && (
            isOnAnswer && backupInfo.answer != null ||
                isOnAnswer.not() && backupInfo.question != null
        )
    }
}