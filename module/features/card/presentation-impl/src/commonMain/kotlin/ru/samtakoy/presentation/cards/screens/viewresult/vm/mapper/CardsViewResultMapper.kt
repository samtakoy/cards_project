package ru.samtakoy.presentation.cards.screens.viewresult.vm.mapper

import org.jetbrains.compose.resources.getString
import ru.samtakoy.domain.learncourse.schedule.Schedule
import ru.samtakoy.domain.view.ViewHistoryItem
import ru.samtakoy.presentation.cards.screens.viewresult.vm.CardsViewResultViewModel.State
import ru.samtakoy.presentation.cards.view.model.CardViewMode
import ru.samtakoy.presentation.base.model.AnyUiId
import ru.samtakoy.presentation.core.design_system.button.usual.MyButtonUiModel
import ru.samtakoy.presentation.utils.asA
import ru.samtakoy.presentation.utils.toStringView
import ru.samtakoy.resources.Res
import ru.samtakoy.resources.action_ok
import ru.samtakoy.resources.cards_view_res_err_cards
import ru.samtakoy.resources.cards_view_res_schedule_none
import ru.samtakoy.resources.cards_view_res_schedule_text
import ru.samtakoy.resources.cards_view_res_title
import ru.samtakoy.resources.cards_view_res_viewed_cards

internal interface CardsViewResultMapper {

    suspend fun map(
        cardViewMode: CardViewMode,
        view: ViewHistoryItem?,
        schedule: Schedule
    ): State.Content?

    companion object {
        val IdEditScheduleBtn = AnyUiId()
        val IdOkBtn = AnyUiId()
    }
}

internal class CardsViewResultMapperImpl : CardsViewResultMapper {

    override suspend fun map(
        cardViewMode: CardViewMode,
        view: ViewHistoryItem?,
        schedule: Schedule
    ): State.Content? {
        view ?: return null
        return State.Content(
            title = getString(Res.string.cards_view_res_title).asA(),
            viewedTitle = getString(
                Res.string.cards_view_res_viewed_cards,
                view.viewedCardIds.size
            ).asA(),
            errorsTitle = if (cardViewMode == CardViewMode.LEARNING) {
                null
            } else {
                getString(
                    Res.string.cards_view_res_err_cards,
                    view.errorCardIds.size
                ).asA()
            },
            scheduleTitle =  if (cardViewMode == CardViewMode.LEARNING) {
                null
            } else {
                getString(Res.string.cards_view_res_schedule_text).asA()
            },
            editScheduleButton =  if (cardViewMode == CardViewMode.LEARNING) {
                null
            } else {
                MyButtonUiModel(
                    id = CardsViewResultMapper.IdEditScheduleBtn,
                    text = if (!schedule.isEmpty) {
                        schedule.toStringView()
                    } else {
                        getString(Res.string.cards_view_res_schedule_none)
                    }.asA(),
                    isEnabled = true
                )
            },
            okButton = MyButtonUiModel(
                id = CardsViewResultMapper.IdOkBtn,
                text = getString(Res.string.action_ok).asA(),
                isEnabled = true
            )
        )
    }
}