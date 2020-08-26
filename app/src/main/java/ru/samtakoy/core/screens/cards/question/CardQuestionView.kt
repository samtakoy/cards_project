package ru.samtakoy.core.screens.cards.question

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface CardQuestionView : MvpView {

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun setQuestionText(text: String?)

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun setPrevCardButtonVisible(visible: Boolean)

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun setViewAnswerButtonVisible(visible: Boolean)

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun setNextCardButtonVisible(visible: Boolean)

    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun showError(codeResId: Int)
}