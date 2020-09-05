package ru.samtakoy.core.presentation.cards.result

import io.reactivex.disposables.CompositeDisposable
import moxy.InjectViewState
import moxy.MvpPresenter
import org.apache.commons.lang3.exception.ExceptionUtils
import ru.samtakoy.R
import ru.samtakoy.core.business.NCoursesInteractor
import ru.samtakoy.core.business.utils.s_io_mainThread
import ru.samtakoy.core.database.room.entities.LearnCourseEntity
import ru.samtakoy.core.database.room.entities.elements.Schedule
import ru.samtakoy.core.presentation.cards.types.CardViewMode
import ru.samtakoy.core.presentation.log.MyLog
import javax.inject.Inject

@InjectViewState
class CardsViewResultPresenter(
        coursesInteractor: NCoursesInteractor,
        val callbacks: Callbacks,
        val learnCourseId: Long,
        val cardViewMode: CardViewMode
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

    private var newErrorCardsSchedule: Schedule = Schedule()
    private val operationDisposable = CompositeDisposable()

    init {

        operationDisposable.add(
                coursesInteractor.getCourse(learnCourseId)
                        .compose(s_io_mainThread())
                        .subscribe(
                                { learnCourse -> initView(learnCourse) },
                                { t: Throwable? -> onGetError(t) }
                        )
        );
    }

    private fun initView(learnCourse: LearnCourseEntity) {
        viewState.setLearnView(cardViewMode === CardViewMode.LEARNING)
        viewState.setViewedCardsCount(learnCourse.getViewedCardsCount())
        viewState.setErrorCardsCount(learnCourse.getErrorCardsCount())
        viewState.showNewScheduleString(newErrorCardsSchedule)
    }

    override fun onDestroy() {

        operationDisposable.dispose()

        super.onDestroy()
    }

    fun getStateToSave(): String {
        return newErrorCardsSchedule.toString()
    }

    fun onRestoreState(state: String) {
        newErrorCardsSchedule.initFromString(state)
        viewState.showNewScheduleString(newErrorCardsSchedule)
    }

    fun onUiScheduleClick() = viewState.showScheduleEditDialog(newErrorCardsSchedule)
    fun onNewScheduleSet(schedule: Schedule) {
        newErrorCardsSchedule = schedule
        viewState.showNewScheduleString(newErrorCardsSchedule)
    }

    fun onUiOkClick() = callbacks.onResultOk(newErrorCardsSchedule);

    private fun onGetError(t: Throwable?) {

        MyLog.add(ExceptionUtils.getMessage(t), t)
        viewState.showError(R.string.db_request_err_message)
    }

}