package ru.samtakoy.core.screens.courses.info

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.SingleStateStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.samtakoy.core.database.room.entities.LearnCourseEntity
import ru.samtakoy.core.screens.cards.types.CardViewMode
import ru.samtakoy.core.screens.cards.types.CardViewSource

interface CourseInfoView : MvpView {

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun showLearnCourseInfo(learnCourse: LearnCourseEntity)

    @StateStrategyType(value = SingleStateStrategy::class)
    fun exit();

    @StateStrategyType(value = SkipStrategy::class)
    fun navigateToCardsViewScreen(learnCourseId: Long, viewSource: CardViewSource, viewMode: CardViewMode)

    @StateStrategyType(value = SkipStrategy::class)
    fun requestExtraordinaryRepeating()

}