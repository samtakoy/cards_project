package ru.samtakoy.core.presentation.courses.select

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.samtakoy.core.data.local.database.room.entities.LearnCourseEntity

interface SelectCourseView : MvpView {

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun showCourses(curCourses: List<LearnCourseEntity>)

    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun showError(codeResId: Int)

    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun exitOk(courseId: Long);

    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun exitCanceled();
}