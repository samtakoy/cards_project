package ru.samtakoy.presentation.cards.screens.viewresult.vm.mapper

import ru.samtakoy.common.resources.Resources
import ru.samtakoy.domain.learncourse.schedule.Schedule
import ru.samtakoy.domain.view.ViewHistoryItem
import ru.samtakoy.presentation.cards.impl.R
import ru.samtakoy.presentation.cards.screens.viewresult.vm.CardsViewResultViewModel.State
import ru.samtakoy.presentation.cards.view.model.CardViewMode
import ru.samtakoy.presentation.core.design_system.base.model.AnyUiId
import ru.samtakoy.presentation.core.design_system.button.usual.MyButtonUiModel
import ru.samtakoy.presentation.utils.asA
import ru.samtakoy.presentation.utils.toStringView

internal interface CardsViewResultMapper {

    fun map(
        cardViewMode: CardViewMode,
        view: ViewHistoryItem?,
        schedule: Schedule
    ): State.Content?

    companion object {
        val IdEditScheduleBtn = AnyUiId()
        val IdOkBtn = AnyUiId()
    }
}

internal class CardsViewResultMapperImpl(
    private val resources: Resources
) : CardsViewResultMapper {

    override fun map(
        cardViewMode: CardViewMode,
        view: ViewHistoryItem?,
        schedule: Schedule
    ): State.Content? {
        view ?: return null
        return State.Content(
            title = resources.getString(R.string.cards_view_res_title).asA(),
            viewedTitle = resources.getString(
                R.string.cards_view_res_viewed_cards,
                view.viewedCardIds.size
            ).asA(),
            errorsTitle = if (cardViewMode == CardViewMode.LEARNING) {
                null
            } else {
                resources.getString(
                    R.string.cards_view_res_err_cards,
                    view.errorCardIds.size
                ).asA()
            },
            scheduleTitle =  if (cardViewMode == CardViewMode.LEARNING) {
                null
            } else {
                resources.getString(R.string.cards_view_res_schedule_text).asA()
            },
            editScheduleButton =  if (cardViewMode == CardViewMode.LEARNING) {
                null
            } else {
                MyButtonUiModel(
                    id = CardsViewResultMapper.IdEditScheduleBtn,
                    text = if (!schedule.isEmpty) {
                        schedule.toStringView(resources)
                    } else {
                        resources.getString(R.string.cards_view_res_schedule_none)
                    }.asA(),
                    isEnabled = true
                )
            },
            okButton = MyButtonUiModel(
                id = CardsViewResultMapper.IdOkBtn,
                text = resources.getString(ru.samtakoy.common.utils.R.string.action_ok).asA(),
                isEnabled = true
            )
        )
    }
}