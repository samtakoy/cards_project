package ru.samtakoy.core.screens.cards.result

import moxy.InjectViewState
import moxy.MvpPresenter
import ru.samtakoy.core.business.NCoursesInteractor
import ru.samtakoy.core.model.elements.Schedule
import ru.samtakoy.core.screens.cards.types.CardViewMode
import javax.inject.Inject

@InjectViewState
class CardsViewResultPresenter(
        coursesInteractor: NCoursesInteractor,
        val callbacks: Callbacks,
        val learnCourseId: Long,
        cardViewMode: CardViewMode
) : MvpPresenter<CardsViewResultView>() {

    interface Callbacks {
        fun onResultOk(newSchedule: Schedule?)
    }

    class Factory @Inject constructor(
            val coursesInteractor: NCoursesInteractor
    ) {

        fun create(
                callbacks: Callbacks,
                learnCourseId: Long,
                cardViewMode: CardViewMode
        ) = CardsViewResultPresenter(coursesInteractor, callbacks, learnCourseId, cardViewMode)
    }

    private var newSchedule: Schedule = Schedule()

    init {
        val learnCourse = coursesInteractor.getCourse(learnCourseId)

        viewState.setLearnView(cardViewMode === CardViewMode.LEARNING)
        viewState.setViewedCardsCount(learnCourse.viewedCardsCount)
        viewState.setErrorCardsCount(learnCourse.errorCardsCount)
        viewState.showNewScheduleString(newSchedule)
    }

    fun getStateToSave(): String {
        return newSchedule.toString()
    }

    fun onRestoreState(state: String) {
        newSchedule.initFromString(state)
        viewState.showNewScheduleString(newSchedule)
    }

    fun onUiScheduleClick() = viewState.showScheduleEditDialog(newSchedule)
    fun onNewScheduleSet(schedule: Schedule) {
        newSchedule = schedule
        viewState.showNewScheduleString(newSchedule)
    }

    fun onUiOkClick() = callbacks.onResultOk(newSchedule);


}