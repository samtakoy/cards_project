package ru.samtakoy.core.screens.courses.list

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.samtakoy.core.model.LearnCourse

interface CoursesListView : MvpView {

    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun showBatchExportToEmailDialog()

    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun showAddCourseDialog(defaultTitle: String)

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun showCourses(curCourses: List<LearnCourse>)

    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun showError(codeResId: Int)

    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun navigateToCourseInfo(courseId: Long)

}