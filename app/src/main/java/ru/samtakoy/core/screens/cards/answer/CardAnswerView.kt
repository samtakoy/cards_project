package ru.samtakoy.core.screens.cards

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface CardAnswerView : MvpView{


    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setAnswerText(text:String):Unit

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setBackButtonVisible(visible: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setWrongButtonVisible(visible: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setNextCardButtonVisible(visible: Boolean)

}