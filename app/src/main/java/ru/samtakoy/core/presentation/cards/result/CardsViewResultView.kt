package ru.samtakoy.core.presentation.cards.result

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.samtakoy.core.data.local.database.room.entities.elements.Schedule

interface CardsViewResultView : MvpView {

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun setLearnView(value: Boolean)

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun setViewedCardsCount(count: Int)

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun setErrorCardsCount(count: Int)

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun showNewScheduleString(schedule: Schedule)

    @StateStrategyType(value = SkipStrategy::class)
    fun showScheduleEditDialog(schedule: Schedule)

    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun showError(stringId: Int)
}