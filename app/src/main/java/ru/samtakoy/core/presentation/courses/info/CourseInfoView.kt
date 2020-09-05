package ru.samtakoy.core.presentation.courses.info

import moxy.MvpView
import moxy.viewstate.strategy.*
import ru.samtakoy.core.database.room.entities.LearnCourseEntity
import ru.samtakoy.core.presentation.cards.types.CardViewMode
import ru.samtakoy.core.presentation.cards.types.CardViewSource

interface CourseInfoView : MvpView {

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun showLearnCourseInfo(learnCourse: LearnCourseEntity)

    @StateStrategyType(value = SingleStateStrategy::class)
    fun exit();

    @StateStrategyType(value = SkipStrategy::class)
    fun navigateToCardsViewScreen(learnCourseId: Long, viewSource: CardViewSource, viewMode: CardViewMode)

    @StateStrategyType(value = SkipStrategy::class)
    fun requestExtraordinaryRepeating()

    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun showError(stringId: Int)

    @StateStrategyType(value = AddToEndSingleStrategy::class, tag = "loading")
    fun blockScreenOnOperation()

    @StateStrategyType(value = AddToEndSingleStrategy::class, tag = "loading")
    fun unblockScreenOnOperation()

}