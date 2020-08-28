package ru.samtakoy.core.presentation.progress_dialog

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface ProgressDialogView : MvpView {

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun showTitle(titleResId: Int)

    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun showError(stringId: Int)

    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun exitCanceled()

    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun exitOk()

}